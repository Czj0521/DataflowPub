package com.bdilab.dataflow.config;

import com.bdilab.dataflow.utils.redis.FastJson2JsonRedisSerialize;
import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Redis Conifg.

 * @author wh
 * @date 2021/10/12
 */
@Configuration
@EnableCaching
public class RedisConifg {

    @Bean
    public RedisSerializer fastJson2JsonRedisSerialize() {
        return new FastJson2JsonRedisSerialize<Object>(Object.class);
    }

    /**
     * redisTemplate.

     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            RedisSerializer fastJson2JsonRedisSerialize) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerialize);
        redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerialize);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * cacheManager.

     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.entryTtl(Duration.ofMinutes(5))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(fastJson2JsonRedisSerialize()))
                .disableCachingNullValues();
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }
}
