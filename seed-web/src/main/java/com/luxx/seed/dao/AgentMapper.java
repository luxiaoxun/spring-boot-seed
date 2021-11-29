package com.luxx.seed.dao;

import com.luxx.seed.model.AgentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgentMapper {
    Long getAgentCount();

    AgentEntity findByIp(String ip);

    List<AgentEntity> getAgentsByType(String type);

    int createAgent(AgentEntity agent);

    void updateAgent(AgentEntity agent);
}
