package com.luxx.engine.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Profile("dev")
public class KafKaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.consumer.topic}")
    private String topic;

    public void sendMessage(String message) {
        log.debug("Try to send message: " + message);
        CompletableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(topic, message);
        future.thenAccept(result -> log.debug("Message sent to topic: {}, offset: {}",
                        result.getRecordMetadata().topic(), result.getRecordMetadata().offset()))
                .exceptionally(exception -> {
                    log.error(exception.toString());
                    return null;
                });
    }
}
