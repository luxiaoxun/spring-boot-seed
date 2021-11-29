package com.luxx.seed.service;

import com.luxx.seed.dao.AgentMapper;
import com.luxx.seed.model.AgentEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by luxiaoxun on 2020/5/18.
 */
@Service
@Slf4j
public class AgentService {
    @Autowired
    private AgentMapper agentMapper;

    public long getAgentCount() {
        return agentMapper.getAgentCount();
    }

    public AgentEntity findByIp(String ip) {
        return agentMapper.findByIp(ip);
    }

    public List<AgentEntity> getAgentsByType(String type) {
        return agentMapper.getAgentsByType(type);
    }

    public int createAgent(AgentEntity agent) {
        Date now = new Date();
        agent.setCreateTime(now);
        agent.setUpdateTime(now);
        return agentMapper.createAgent(agent);
    }

}
