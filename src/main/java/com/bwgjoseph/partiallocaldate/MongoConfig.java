package com.bwgjoseph.partiallocaldate;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig {
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
            new MongoOffsetDateTimeReader(),
            new MongoOffsetDateTimeWriter(),
            new MongoPartialLocalDateWriter(),
            new MongoPartialLocalDateReader()));
    }
}
