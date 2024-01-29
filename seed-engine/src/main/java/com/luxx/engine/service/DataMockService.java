package com.luxx.engine.service;

import com.google.gson.JsonObject;
import com.luxx.engine.constant.Constant;
import com.luxx.engine.kafka.KafKaProducer;
import com.luxx.engine.model.event.LogEvent;
import com.luxx.util.CommonUtil;
import com.luxx.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@Profile("dev")
public class DataMockService {
    @Autowired
    private KafKaProducer kafKaProducer;

    private final String deviceId = CommonUtil.getMd5("172.17.6.108");

    @Async
    public void sendNtaMockData(String jsonData) {
        try {
            kafKaProducer.sendMessage(jsonData);
            HashMap<String, Object> map = JsonUtil.jsonToObjectHashMap(jsonData, String.class, Object.class);
            LogEvent data = new LogEvent(map);
            log.info("Send log data: {}", jsonData);
        } catch (Exception e) {
            log.error("Error on send log data: ", e);
        }
    }

    @Async
    public void sendNtaMockData(int threadNum, int count) {
        AtomicInteger totalCount = new AtomicInteger(0);
        //benchmark test
        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; ++i) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < count; i++) {
                        try {
                            JsonObject dataMsg = generateMockData();
                            kafKaProducer.sendMessage(dataMsg.toString());
                            Thread.sleep(10);
                            log.debug("Send log data count: {}", totalCount.incrementAndGet());
                        } catch (Exception e) {
                            log.error("Error on send log data: ", e);
                        }
                    }
                }
            });
            threads[i].start();
        }
    }

    private JsonObject generateMockData() {
        long now = System.currentTimeMillis();
        JsonObject dataLog = new JsonObject();
        dataLog.addProperty(Constant.EVENT_TIME, now);

        dataLog.addProperty(Constant.TENANT_ID, "root");
        dataLog.addProperty(Constant.DEVICE_ID, "f76e3552-976c-3263-5c5b-3063c8ba3dd3");
        dataLog.addProperty(Constant.DEVICE_IP, "10.17.1.56");
        dataLog.addProperty(Constant.MESSAGE, "test-test");

        return dataLog;
    }

    private int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private String getRandomLoginIp() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        return "172.19." + r.nextInt(10, 12) + "." + r.nextInt(100, 255);
    }

    private String getRandomInnerIp() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        return "172.19." + r.nextInt(1, 21) + "." + r.nextInt(100, 255);
    }

    private String getRandomOuterIp() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        return "202.101." + r.nextInt(12, 13) + "." + r.nextInt(200, 255);
    }

}
