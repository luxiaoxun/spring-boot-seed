package com.luxx.seed.service.agent;

import com.luxx.seed.mapper.agent.AgentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by luxiaoxun on 2020/5/18.
 */
@Service
public class AgentService {
    @Autowired
    private AgentMapper agentMapper;

    public long getAgentNumber(){
        return agentMapper.countAgentNumber();
    }
}
