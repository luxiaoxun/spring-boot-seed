package com.luxx.engine.data;

import com.codahale.metrics.Gauge;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.luxx.engine.model.DataDoc;
import com.luxx.engine.model.event.RingBufferEvent;
import com.luxx.util.JsonUtil;
import com.luxx.util.NamedThreadFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EventConsumer implements MessageConsumer {
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private Disruptor<RingBufferEvent> disruptor;
    public static final String listenerId = "data-consumer";

    @Autowired
    private DataProcessor dataProcessor;

    @PostConstruct
    public void init() {
        int bufferSize = 1024;
        initLogDisruptor(bufferSize);
        registerMetric();
    }

    private void initLogDisruptor(int bufferSize) {
        disruptor = new Disruptor<>(RingBufferEvent.FACTORY, bufferSize,
                new NamedThreadFactory("message-process", true),
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        disruptor.setDefaultExceptionHandler(new LoggingExceptionHandler(log));
        EventHandlerGroup<RingBufferEvent> handlerGroup =
                disruptor.handleEventsWithWorkerPool(new DataHandler(dataProcessor));

    }

    public boolean eventRingBufferFull() {
        return disruptor.getRingBuffer().remainingCapacity() <= 0;
    }

    private void registerMetric() {
        Gauge<Long> eventRingBufferCapacity = () -> disruptor.getRingBuffer().remainingCapacity();
    }

    @Override
    public void start() {
        disruptor.start();
        if (!kafkaListenerEndpointRegistry.getListenerContainer(listenerId).isRunning()) {
            kafkaListenerEndpointRegistry.getListenerContainer(listenerId).start();
        }
    }

    @Override
    public void stop() {
        kafkaListenerEndpointRegistry.getListenerContainer(listenerId).stop();
        disruptor.shutdown();
    }

    @KafkaListener(id = listenerId, topics = "${kafka.consumer.topic}",
            containerFactory = "eventListenerContainerFactory",
            groupId = "${kafka.consumer.group.id}")
    public void consume(List<String> messages, Acknowledgment ack) {
        try {
            messages.forEach(msg -> {
                try {
                    log.debug(String.format("Message received -> %s", msg));
                    Map<String, Object> data = JsonUtil.jsonToObjectHashMap(msg, String.class, Object.class);
                    DataDoc dataDoc = new DataDoc(data);
                    disruptor.publishEvent(RingBufferEvent.TRANSLATOR, dataDoc);
                } catch (Exception ex) {
                    log.error("Can not process message:{}, error:{}", msg, ex.toString());
                }
            });
            //ack失败会有异常
            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Consume message error: " + ex.toString());
        }
    }
}
