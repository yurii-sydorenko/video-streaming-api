package com.videostreaming.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration responsible for SerializationFeature.FAIL_ON_EMPTY_BEAN disabling.
 * It's required to successfully return a video in VideoController.loadVideo response body
 * @author Yurii Sydorenko
 */
@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

}
