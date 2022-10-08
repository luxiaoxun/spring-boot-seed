package com.luxx.seed.dao;

import com.luxx.seed.model.Agent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgentMapper {
    Long getAgentCount();

    Agent getAgentByIp(String ip);

    List<Agent> getAgentsByType(String type, String order, String direction);

    int createAgent(Agent agent);

    void updateAgent(Agent agent);
}
