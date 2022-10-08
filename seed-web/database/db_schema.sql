CREATE TABLE IF NOT EXISTS tb_agent
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    ip           VARCHAR(64)  COMMENT 'IP地址',
    version      VARCHAR(64)  COMMENT '版本号',
    status       TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '状态',
    type         VARCHAR(64)  COMMENT 'linux|windows',
    owner        VARCHAR(64)  COMMENT '机器所属人',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tb_tenant
(
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    name         VARCHAR(256)  COMMENT '租户名称',
    address      VARCHAR(512)  COMMENT '租户地址',
    status       TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '状态，1:有效，0:无效',
    create_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tb_user
(
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    tenant_id    BIGINT        COMMENT '租户ID',
    name         VARCHAR(128)  COMMENT '用户名',
    passwd       VARCHAR(256)  COMMENT '密码',
    is_admin     TINYINT(1)    NOT NULL DEFAULT 0 COMMENT '是否租户管理员',
    status       TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '状态，1:有效，0:无效',
    create_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;