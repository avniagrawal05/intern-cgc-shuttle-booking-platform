package com.hms.config;

import com.hms.constants.AppConstants;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Redis configuration for caching
 */
@Configuration
@EnableCaching
public class RedisConfig {
    
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(AppConstants.CACHE_DEFAULT_TTL))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
        
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration(AppConstants.CACHE_PATIENTS, 
                        cacheConfig.entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration(AppConstants.CACHE_DOCTORS, 
                        cacheConfig.entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration(AppConstants.CACHE_APPOINTMENTS, 
                        cacheConfig.entryTtl(Duration.ofMinutes(15)))
                .withCacheConfiguration(AppConstants.CACHE_BILLINGS, 
                        cacheConfig.entryTtl(Duration.ofMinutes(30)))
                .build();
    }
}
