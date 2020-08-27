package com.luxx.seed.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentMapper {
    long countAgentNumber();
}
