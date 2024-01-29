package com.luxx.engine.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaLagService {
    @Value("${kafka.consumer.bootstrap-servers}")
    private String kafkaServers;

    @Autowired
    private KafkaConfig kafkaConfig;

    private AdminClient adminClient;
    private KafkaConsumer<String, String> consumer;

    @PostConstruct
    public void init() {
        adminClient = getAdminClient(kafkaServers);
        consumer = getKafkaConsumer(kafkaServers);
    }

    @PreDestroy
    public void destroy() {
        if (consumer != null) {
            consumer.close();
        }
        if (adminClient != null) {
            adminClient.close();
        }
    }

    public long getTotalPartitionLag(String groupId) {
        try {
            Map<TopicPartition, Long> lags = analyzeLag(groupId);
            if (lags == null) {
                return 0L;
            }
            return lags.values().stream()
                    .mapToLong(lag -> lag)
                    .sum();
        } catch (Exception e) {
            log.error("Analyze lag error: " + e.toString());
            return 0L;
        }
    }

    public long getMaxPartitionLag(String groupId) {
        try {
            Map<TopicPartition, Long> lags = analyzeLag(groupId);
            if (lags == null) {
                return 0L;
            }
            return lags.values().stream()
                    .mapToLong(lag -> lag)
                    .max()
                    .orElse(0L);
        } catch (Exception e) {
            log.error("Analyze lag error: " + e.toString());
            return 0L;
        }
    }

    public Map<TopicPartition, Long> analyzeLag(String groupId) throws ExecutionException, InterruptedException {
        Map<TopicPartition, Long> consumerGroupOffsets = getConsumerGroupOffsets(groupId);
        Map<TopicPartition, Long> logEndOffsets = getLogEndOffsets(consumerGroupOffsets);
        Map<TopicPartition, Long> lags = computeLags(consumerGroupOffsets, logEndOffsets);
        for (Map.Entry<TopicPartition, Long> lagEntry : lags.entrySet()) {
            String topic = lagEntry.getKey().topic();
            int partition = lagEntry.getKey().partition();
            Long lag = lagEntry.getValue();
            log.info("Kafka lag for topic = {}, partition = {} is {}", topic, partition, lag);
        }
        return lags;
    }

    public Map<TopicPartition, Long> getConsumerGroupOffsets(String groupId) throws ExecutionException, InterruptedException {
        ListConsumerGroupOffsetsResult info = adminClient.listConsumerGroupOffsets(groupId);
        Map<TopicPartition, OffsetAndMetadata> metadataMap = info.partitionsToOffsetAndMetadata().get();
        Map<TopicPartition, Long> groupOffset = new HashMap<>();
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : metadataMap.entrySet()) {
            TopicPartition key = entry.getKey();
            OffsetAndMetadata metadata = entry.getValue();
            log.info("Consumer offset for topic = {}, partition = {} is {}", key.topic(), key.partition(), metadata.offset());
            groupOffset.putIfAbsent(new TopicPartition(key.topic(), key.partition()), metadata.offset());
        }
        return groupOffset;
    }

    private Map<TopicPartition, Long> getLogEndOffsets(Map<TopicPartition, Long> consumerGroupOffset) {
        List<TopicPartition> topicPartitions = new LinkedList<>();
        for (Map.Entry<TopicPartition, Long> entry : consumerGroupOffset.entrySet()) {
            TopicPartition key = entry.getKey();
            topicPartitions.add(new TopicPartition(key.topic(), key.partition()));
        }
        return consumer.endOffsets(topicPartitions);
    }

    public Map<TopicPartition, Long> computeLags(Map<TopicPartition, Long> consumerGroupOffsets,
                                                 Map<TopicPartition, Long> logEndOffsets) {
        Map<TopicPartition, Long> lags = new HashMap<>();
        for (Map.Entry<TopicPartition, Long> entry : consumerGroupOffsets.entrySet()) {
            Long producerOffset = logEndOffsets.get(entry.getKey());
            Long consumerOffset = consumerGroupOffsets.get(entry.getKey());
            long lag = Math.abs(Math.max(0, producerOffset) - Math.max(0, consumerOffset));
            lags.putIfAbsent(entry.getKey(), lag);
        }
        return lags;
    }

    private AdminClient getAdminClient(String bootstrapServerConfig) {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
        kafkaConfig.updateSaslConfig(config);
        return AdminClient.create(config);
    }

    private KafkaConsumer<String, String> getKafkaConsumer(String bootstrapServerConfig) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaConfig.updateSaslConfig(properties);
        return new KafkaConsumer<>(properties);
    }
}
