package com.luxx.service.zookeeper;

import com.luxx.service.config.ConfigService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Lazy
@Component
public class CuratorClient implements InitializingBean {

    @Qualifier("NoCache")
    @Autowired
    private ConfigService configService;

    private CuratorFramework client;

    @Override
    public void afterPropertiesSet() throws Exception {
        String namespace = configService.selectConfig("zookeeper.namespace").getValue();
        String connectString = configService.selectConfig("zookeeper.connectstring").getValue();

        client = CuratorFrameworkFactory.builder().namespace(namespace).connectString(connectString)
                .retryPolicy(new ExponentialBackoffRetry(1000, 600)).build();
        client.start();
    }

    public CuratorFramework getClient() {
        return client;
    }

    @PreDestroy
    public void close() {
        client.close();
    }
}
