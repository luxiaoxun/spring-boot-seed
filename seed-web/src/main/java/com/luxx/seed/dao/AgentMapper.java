package com.luxx.seed.dao;

import com.luxx.seed.model.AgentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentMapper {
    long countAgentNumber();

    AgentEntity findByIp(String ip);
}
