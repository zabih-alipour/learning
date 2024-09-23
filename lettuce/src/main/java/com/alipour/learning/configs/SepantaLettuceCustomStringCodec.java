package com.alipour.learning.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.codec.RedisCodec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SepantaLettuceCustomStringCodec implements RedisCodec<String, Object> {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return charset.decode(bytes).toString();
    }

    @Override
    public Object decodeValue(ByteBuffer bytes) {
        try {
            return mapper.readValue(bytes.array(), Object.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return charset.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(Object value) {
        try {
            return ByteBuffer.wrap(mapper.writeValueAsBytes(value));
        } catch (IOException e) {
            return ByteBuffer.wrap(new byte[0]);
        }
    }
}

