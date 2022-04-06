package com.cloud.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@Configuration
public class StatsD {

    @Value("true")
    private boolean publishMetrics;
    @Value("localhost")
    private String host;
    @Value("8125")
    private int port;

    @Bean
    public StatsDClient metricsClient() {
        if(publishMetrics){
            return new NonBlockingStatsDClient("csye6225", host, port);
        }
        return new NoOpStatsDClient();
    }



}