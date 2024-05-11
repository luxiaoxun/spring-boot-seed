CREATE TABLE IF NOT EXISTS tb_sys_tenant
(
    id           VARCHAR(64)   NOT NULL COMMENT '租户组织ID',
    parent_id    VARCHAR(64)   COMMENT '父租户组织ID',
    name         VARCHAR(128)  NOT NULL COMMENT '名称',
    address      VARCHAR(512)  COMMENT '地址',
    status       TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '状态，0:无效，1:有效',
    create_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tb_sys_user
(
    id            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '账号ID',
    username      VARCHAR(64)   NOT NULL COMMENT '账号',
    password      VARCHAR(64)   NOT NULL COMMENT '密码',
    tenant_id     JSON          COMMENT '组织ID',
    status        TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '状态，0:无效，1:有效，2:删除，3:锁定',
    gender        TINYINT(1)    COMMENT '性别，1:男，2:女，3:其他',
    mobile_phone  VARCHAR(11)   COMMENT '手机号',
    email         VARCHAR(64)   COMMENT '邮箱',
    create_user   VARCHAR(64)   COMMENT '创建用户',
    update_user   VARCHAR(64)   COMMENT '更新用户',
    create_time   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE INDEX username (username)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tb_sys_role
(
    id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    name          VARCHAR(64) NOT NULL COMMENT '角色名称',
    builtin       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '0-自定义，1-系统内置',
    remark        TEXT        COMMENT '备注',
    create_user   VARCHAR(64) COMMENT '创建用户',
    update_user   VARCHAR(64) COMMENT '更新用户',
    create_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE INDEX name (name)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tb_sys_user_role  (
    id       BIGINT  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id  BIGINT  NOT NULL COMMENT '用户ID',
    role_id  BIGINT  NOT NULL COMMENT '角色ID',
    PRIMARY KEY (id),
    INDEX idx_sys_user_role_uid (user_id),
    INDEX idx_sys_user_role_rid (role_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tb_sys_menu
(
    id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    parent_id     BIGINT      NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    name          VARCHAR(64) NOT NULL COMMENT '菜单名称',
    code          VARCHAR(64) NOT NULL COMMENT '菜单唯一编码',
    path          VARCHAR(64) NOT NULL COMMENT '路由',
    PRIMARY KEY (id),
    UNIQUE INDEX code (code)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tb_sys_role_menu
(
    id       BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_id  BIGINT NOT NULL COMMENT '角色ID',
    menu_id  BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (id),
    INDEX idx_role_menu_rid (role_id),
    INDEX idx_role_menu_mid (menu_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tb_agent
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    tenant_id    VARCHAR(64)  NOT NULL COMMENT '租户ID',
    ip           VARCHAR(64)  COMMENT 'IP地址',
    version      VARCHAR(64)  COMMENT '版本号',
    status       TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '状态',
    type         VARCHAR(64)  COMMENT 'linux|windows',
    owner        VARCHAR(64)  COMMENT '机器所属人',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;