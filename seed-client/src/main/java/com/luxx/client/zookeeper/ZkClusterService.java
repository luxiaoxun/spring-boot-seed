package com.luxx.client.zookeeper;

import java.util.Collection;
import java.util.concurrent.Executor;

import com.luxx.util.OsUtil;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class ZkClusterService implements InitializingBean {

    @Autowired
    private CuratorClient curatorClient;

    @Autowired
    private Executor executor;

    private LeaderLatch leaderLatch;

    private final String type;

    public ZkClusterService(String type) {
        super();
        this.type = type;
    }

    private static final Logger logger = LoggerFactory.getLogger(ZkClusterService.class);

    @Override
    public void afterPropertiesSet() throws Exception {

        leaderLatch = new LeaderLatch(curatorClient.getClient(), "/master/" + type, OsUtil.getIPS());
        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void notLeader() {
                logger.info("I am not master[" + type + "].");
                try {
                    logger.info("Master is " + leaderLatch.getLeader());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            @Override
            public void isLeader() {
                logger.info("I am master[" + type + "].");
            }

        }, executor);
        leaderLatch.start();
    }

    public boolean isMaster() {
        return leaderLatch.hasLeadership();
    }

    public String getMaster() throws Exception {
        return leaderLatch.getLeader().getId();
    }

    public Collection<Participant> getParticipants() throws Exception {
        return leaderLatch.getParticipants();
    }
}
