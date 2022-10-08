package com.luxx.seed.service;

import com.luxx.seed.dao.AgentMapper;
import com.luxx.seed.model.Agent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AgentService {
    @Autowired
    private AgentMapper agentMapper;

    public long getAgentCount() {
        return agentMapper.getAgentCount();
    }

    public Agent findByIp(String ip) {
        return agentMapper.getAgentByIp(ip);
    }

    public List<Agent> getAgentsByType(String type, String order, String direction) {
        return agentMapper.getAgentsByType(type, order, direction);
    }

    public int createAgent(Agent agent) {
        Date now = new Date();
        agent.setCreateTime(now);
        agent.setUpdateTime(now);
        return agentMapper.createAgent(agent);
    }

}
