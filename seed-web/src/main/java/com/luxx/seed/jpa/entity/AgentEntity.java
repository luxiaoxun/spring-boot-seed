package com.luxx.seed.jpa.entity;

import com.luxx.seed.constant.os.OsEnum;
import com.luxx.seed.service.os.OsEnumConvertService;
import com.luxx.seed.constant.os.OsEnum;
import com.luxx.seed.service.os.OsEnumConvertService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "chaos_agent_list")
@Accessors(chain = true)
public class AgentEntity extends BaseEntity implements Serializable {

    private String ip;

    private String username;

    private String password;

    private String version;

    private String status;

    private String owner;

    @Convert(converter = OsEnumConvertService.class)
    private OsEnum os;

    @Column(name = "agent_installed")
    private boolean agentInstalled;

    @Column(name = "create_time")
    private java.util.Date createTime;

    @Column(name = "update_time")
    private java.util.Date updateTime;

    private String type;

}
