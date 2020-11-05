package com.luxx.seed.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.luxx.seed.constant.OsEnum;
import com.luxx.seed.service.OsEnumConvertService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_agent")
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

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;

    private String type;

}
