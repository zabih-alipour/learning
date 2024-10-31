package com.alipour.learning.springweb_keycloak_policy_enforcer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.Arrays;

@SpringBootApplication
public class SpringWebKeycloakPolicyEnforcerApplication implements CommandLineRunner {

	@Autowired
	private ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(SpringWebKeycloakPolicyEnforcerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
	}
}
