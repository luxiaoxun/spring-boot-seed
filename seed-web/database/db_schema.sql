CREATE TABLE IF NOT EXISTS tb_agent
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    ip           VARCHAR(64)  COMMENT 'IP地址',
    version      VARCHAR(64)  COMMENT '版本号',
    status       TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '状态',
    type         VARCHAR(64)  COMMENT 'linux|windows',
    owner        VARCHAR(64)  COMMENT '机器所属人',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1001
  CHARSET = utf8mb4;