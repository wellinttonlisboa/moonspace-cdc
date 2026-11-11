/*
 *  @(#)ReadyEventListener.java
 *
 *  Copyright (c) Moonspace
 *  All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of Moonspace.
 *  ("Confidential Information"). You shall not disclose such Confidential
 *  Information and shall use it only in accordance with the terms of the
 *  license agreement you entered into with Moonspace.
 *
 */
package br.com.moonspace.cdc.router.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * @author contato@moonspace.com.br
 * @class ReadyEventListener
 */
@Configuration
public class ReadyEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ReadyEventListener.class);

    @Value("${application.environment}")
    private String environment;

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private String redisPort;
    @Value("${spring.data.redis.database}")
    private String redisDatabase;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String kafkaProducerServer;
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String kafkaConsumerServer;


    /**
     * The method executed after Spring Boot is ready to be used.
     *
     * @param event Ready event.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void start(ApplicationReadyEvent event) {
        LOG.info(">>> Connector Ready");
        show();
    }

    private void show() {
        LOG.info("======================================================");
        LOG.info("== Execute mode:.....................'{}'", environment);
        LOG.info("== Connect Redis Host:...............'{}'", redisHost);
        LOG.info("== Connect Redis Port:...............'{}'", redisPort);
        LOG.info("== Connect Redis Database:...........'{}'", redisDatabase);
        LOG.info("== Connect Kafka Producer Broker:....'{}'", kafkaProducerServer);
        LOG.info("== Connect Kafka Consumer Broker:....'{}'", kafkaConsumerServer);
        LOG.info("======================================================");
    }

}