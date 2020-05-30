package com.luxx.seed.mapper.agent;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentMapper {
    long countAgentNumber();
}
