package com.alipour.learning;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ContextPathCompositeHandler;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.*;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.StringUtils;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;

@SpringBootApplication()
@EmbeddedKafka(bootstrapServersProperty = "spring.kafka.bootstrap-servers")
public class ReadChannelExample {

    @Autowired
    @Qualifier("dataInputChannel")
    private MessageChannel dataInputChannel;
    @Autowired
    @Qualifier("dataOutputChannel")
    private MessageChannel dataOutputChannel;
    @Autowired
    @Qualifier("errorChannel")
    private MessageChannel errorChannel;
    @Autowired
    @Qualifier("dataInputQueueChannel")
    private QueueChannel dataInputQueueChannel;
    @Autowired
    @Qualifier("dataInputPubSubChannel")
    private PublishSubscribeChannel dataInputPubSubChannel;
    @Autowired
    private IntegrationFlowContext integrationFlowContext;
    @Autowired
    private KafkaTemplate<String, String> producerTemplate;

    @Autowired
    ApplicationContext applicationContext;

    public static List<String> categories = List.of("Z1", "T1", "Z2", "M3", "11", "15");

    @Bean
    CommandLineRunner readChannelMessageExampleRunner() {
        return args -> this.execute();
    }

    @Configuration
    static class AppConfig {
        @Bean("dataInputChannel")
        public MessageChannel dataInputChannel() {
            return MessageChannels.flux().getObject();
        }

        @Bean("dataOutputChannel")
        public MessageChannel dataOutputChannel() {
            return MessageChannels.flux().getObject();
        }

        @Bean("errorChannel")
        public MessageChannel errorChannel() {
            final FluxMessageChannel object = MessageChannels.flux().getObject();
            return object;
        }

        @Bean("dataInputQueueChannel")
        public QueueChannel dataInputQueueChannel() {
            final QueueChannel object = MessageChannels.queue().getObject();
            return object;
        }

        @Bean("dataInputPubSubChannel")
        public PublishSubscribeChannel dataInputPubSubChannel() {
            final PublishSubscribeChannel object = MessageChannels.publishSubscribe().getObject();
            object.setMinSubscribers(1);
            return object;
        }

        @Bean(name = PollerMetadata.DEFAULT_POLLER)
        public PollerSpec poller() {
            return Pollers.fixedDelay(Duration.ofNanos(1), Duration.ofNanos(1));
        }

        @Bean
        CommandLineRunner publishGroupMessage(@Qualifier("dataInputChannel") MessageChannel messageChannel) {
            final Random random = new Random();
            return args -> {
                Flux.fromIterable(categories)
                        .delayElements(Duration.ofSeconds(2))
                        .map(i -> {
                            final String group = categories.get(random.nextInt(0, categories.size() - 1));
                            String messagePayload = "Group of " + group + " is published";
                            return MessageBuilder.withPayload(messagePayload).build();
                        })
                        .subscribe(messageChannel::send);
            };
        }

        @Bean
        public HttpHandler httpHandler(ObjectProvider<WebFluxProperties> propsProvider,ApplicationContext applicationContext,
                                       ObjectProvider<WebHttpHandlerBuilderCustomizer> handlerBuilderCustomizers) {
            WebHttpHandlerBuilder handlerBuilder = WebHttpHandlerBuilder.applicationContext(applicationContext);
            handlerBuilderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(handlerBuilder));
            HttpHandler httpHandler = handlerBuilder.build();
            WebFluxProperties properties = propsProvider.getIfAvailable();
            if (properties != null && StringUtils.hasText(properties.getBasePath())) {
                Map<String, HttpHandler> handlersMap = Collections.singletonMap(properties.getBasePath(), httpHandler);
                return new ContextPathCompositeHandler(handlersMap);
            }
            return httpHandler;
        }
        @Bean
        public ConsumerFactory<String, String> kafkaConsumerFactory(@Value("${spring.kafka.bootstrap}") String bootstrap) {
            Map<String, Object> map = new HashMap<>() {{
                put(ConsumerConfig.GROUP_ID_CONFIG, "GIC_1");
                put(ConsumerConfig.CLIENT_ID_CONFIG, "CIF_1");
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serdes.String().deserializer().getClass());
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Serdes.String().deserializer().getClass());
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
            }};
            return new DefaultKafkaConsumerFactory<>(map);
        }

        @Bean
        public ProducerFactory<String, String> kafkaProducerFactory(@Value("${spring.kafka.bootstrap}") String bootstrap) {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());
            return new DefaultKafkaProducerFactory<>(props);
        }

        @Bean
        public KafkaTemplate<String, String> kafkaProducerTemplate(ProducerFactory<String, String> kafkaProducerFactory) {
            return new KafkaTemplate<>(kafkaProducerFactory);
        }
    }

    public void execute() {
        final Random random = new Random();
        Flux.interval(Duration.ofMillis(500))
                .map(milli -> {
                    final String group = categories.get(random.nextInt(0, categories.size() - 1));
                    String messagePayload = "Message " + milli + " is belongs to group " + group;
                    return new ProducerRecord<>("RAW_MESSAGE", group, messagePayload);
                })
                .subscribe(producerTemplate::send);
    }


    @MessagingGateway(defaultRequestChannel = "dataInputChannel", errorChannel = "errorChannel")
    interface MyGateway {
        @Gateway
        void send(String input);
    }

    @Bean
    public IntegrationFlow consumeKafkaTopic(ConsumerFactory<String, String> consumerFactory) {
        final ContainerProperties containerProperties = new ContainerProperties("RAW_MESSAGE");
        containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);
        return IntegrationFlow.from(Kafka.messageDrivenChannelAdapter(consumerFactory, containerProperties))
                .handle(message -> {
                    ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) message.getPayload();
                    System.out.println("Consumed message [ " + record.value() + " ] from kafka");
                    final String beanName = "group-queue-channel-" + record.key();
                    if (applicationContext.containsBean(beanName)) {
                        QueueChannel channel = applicationContext.getBean(beanName, QueueChannel.class);
                        channel.send(MessageBuilder.withPayload(record.value()).build());
                    } else {
                        final QueueChannel channel = MessageChannels.queue().getObject();
                        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
                        beanFactory.registerSingleton(beanName, channel);
                        channel.send(MessageBuilder.withPayload(record.value()).build());
                    }
                })
                .get();
    }

    @Bean
    public IntegrationFlow consumeDataOutputChannel() {
        return IntegrationFlow.from(dataOutputChannel)
                .<String, String>transform(String::toUpperCase)
                .handle(message -> System.out.println(" dataOutputChannel message = " + message))
                .get();
    }

    @ServiceActivator(inputChannel = "dataInputChannel")
    public void logDataInputChannel(Message<String> message) {
        if (message.getPayload().contains("11")) {
            System.out.println("Consumed group message = " + message);
            final String group = message.getPayload().split(" ")[2];
            final String channelName = "group-queue-channel-" + group;
            final StandardIntegrationFlow flow = IntegrationFlow.from(channelName)
                    .<String, String>transform(String::toUpperCase)
                    .handle(message1 -> System.out.println("Received message = " + message1))
//                    .channel("dataOutputChannel")
                    .get();

            System.out.println("isRunning = " + flow.isRunning());
            System.out.println("isAutoStartup = " + flow.isAutoStartup());
            final int remainingCapacity = dataInputQueueChannel.getQueueSize();
            System.out.println("remainingCapacity = " + remainingCapacity);
            integrationFlowContext.registration(flow).register();
        }
    }

    @ServiceActivator(inputChannel = "dataInputChannel")
    public void logDataInputChannel2(Message<String> message) {
        System.out.println("dataInputChannel message = " + message);
    }

//    @ServiceActivator(inputChannel = "dataOutputChannel")
//    public void logDataOutputChannel(Message<String> message) {
//        System.out.println("dataOutputChannel message = " + message);
//    }

    @ServiceActivator(inputChannel = "errorChannel")
    public void logErrorChannel(Exception e) {
        System.out.println("errorChannel message = " + e);
    }


    public static void main(String[] args) {
        SpringApplication.run(ReadChannelExample.class, args);
    }
}
