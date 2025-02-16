package com.spring.marketplace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import redis.clients.jedis.JedisPool;

@Configuration
@EnableAutoConfiguration(exclude = {
        JmxAutoConfiguration.class
})
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.jedis.pool.port}")
    private int port;
    @Value("${spring.redis.jedis.pool.host}")
    private String host;

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(host,port);
    }
}
