package com.luxx.engine.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {
    @Value("${kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer.group.id}")
    private String groupId;

    @Value("${kafka.consumer.concurrency}")
    private Integer concurrency;

    @Value("${system.config.ring_buffer_size:1024}")
    private Integer bufferSize;

    @Autowired
    private KafkaConfig kafkaConfig;

    public Map<String, Object> consumerConfigs() {
        log.info("consumer bootstrap-servers: " + bootstrapServers);
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 600000); //poll timeout
        config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 5000);  //heartbeat timeout
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);   //session timeout
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 512);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaConfig.updateSaslConfig(config);
        return config;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = consumerConfigs();
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, bufferSize / concurrency);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean("eventListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> eventListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        log.info("Kafka consumer log concurrency: " + concurrency);
        factory.setConcurrency(concurrency);
        factory.setAutoStartup(false);
        factory.setBatchListener(true);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
