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
    id                   BIGINT        NOT NULL AUTO_INCREMENT COMMENT '账号ID',
    username             VARCHAR(64)   NOT NULL COMMENT '账号',
    password             VARCHAR(64)   NOT NULL COMMENT '密码',
    tenant_id            JSON          COMMENT '组织ID',
    status               TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '状态，0:无效，1:有效，2:删除，3:锁定',
    gender               TINYINT(1)    COMMENT '性别，1:男，2:女，3:其他',
    mobile_phone         VARCHAR(11)   COMMENT '手机号',
    email                VARCHAR(64)   COMMENT '邮箱',
    login_time           DATETIME      COMMENT '最后登录时间',
    login_attempts       TINYINT(1)    DEFAULT 0 COMMENT '登录尝试次数',
    locked_time          DATETIME      COMMENT '锁定时间',
    password_update_time DATETIME      COMMENT '密码修改时间',
    create_user          VARCHAR(64)   COMMENT '创建用户',
    update_user          VARCHAR(64)   COMMENT '更新用户',
    create_time          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE INDEX username (username)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tb_sys_role
(
    id            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    name          VARCHAR(64) NOT NULL COMMENT '角色名称',
    cn_name       VARCHAR(64) NOT NULL COMMENT '角色中文名称',
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
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    parent_id     BIGINT       NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    name          VARCHAR(64)  NOT NULL COMMENT '菜单名称',
    code          VARCHAR(64)  NOT NULL COMMENT '权限标识',
    type          VARCHAR(32)  NOT NULL COMMENT '菜单类型',
    path          VARCHAR(256) NOT NULL COMMENT '菜单路劲',
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

# 租户组织
INSERT INTO tb_sys_tenant (id, parent_id, name, address, status, create_time, update_time) VALUES('root', NULL, '根组织', NULL, 1, '2024-05-10 17:26:58', '2024-05-10 17:26:58');

# 用户
INSERT INTO tb_sys_user (id, username, password, tenant_id, status, gender, mobile_phone, email, create_user, update_user, create_time, update_time) VALUES(1, 'admin', 'ad89b64d66caa8e30e5d5ce4a9763f4ecc205814c412175f3e2c50027471426d', '["root"]', 1, NULL, NULL, NULL, 'admin', 'admin', '2024-05-10 17:22:44', '2024-05-10 17:22:44');
INSERT INTO tb_sys_user (id, username, password, tenant_id, status, gender, mobile_phone, email, create_user, update_user, create_time, update_time) VALUES(2, 'test-user', 'ad89b64d66caa8e30e5d5ce4a9763f4ecc205814c412175f3e2c50027471426d', '["root"]', 1, NULL, NULL, NULL, 'admin', 'admin', '2024-05-10 17:22:44', '2024-05-10 17:22:44');

# 角色
INSERT INTO tb_sys_role (id, name, cn_name, builtin, remark, create_user, update_user, create_time, update_time) VALUES(1, 'admin', '管理员', 1, NULL, 'admin', 'admin', '2024-05-10 17:27:50', '2024-05-10 17:27:50');
INSERT INTO tb_sys_role (id, name, cn_name, builtin, remark, create_user, update_user, create_time, update_time) VALUES(2, 'analyst', '分析师', 1, NULL, 'admin', 'admin', '2024-05-10 17:29:13', '2024-05-10 17:29:13');
INSERT INTO tb_sys_role (id, name, cn_name, builtin, remark, create_user, update_user, create_time, update_time) VALUES(3, 'auditor', '审计员', 1, NULL, 'admin', 'admin', '2024-05-10 17:29:42', '2024-05-10 17:29:42');

# 用户角色
INSERT INTO tb_sys_user_role (id, user_id, role_id) VALUES(1, 1, 1);
INSERT INTO tb_sys_user_role (id, user_id, role_id) VALUES(2, 2, 2);

# 菜单
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(1, 0, '首页', 'home-page', 'menu', '/home-page');
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(2, 0, '威胁告警', 'attack', 'menu', '/attack');
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(3, 0, '资产管理', 'asset', 'menu', '/asset');
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(4, 0, '探针管理', 'agent', 'menu', '/agent');
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(5, 0, '系统管理', 'system', 'menu', '/system');
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(21, 2, '外网攻击', 'attack:wan', 'menu', '/attack/wan');
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(22, 2, '内部失陷', 'attack:inner', 'menu', '/attack/inner');
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(31, 3, '资产类型', 'asset:type', 'menu', '/asset/type');
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(32, 3, '资产列表', 'asset:list', 'menu', '/asset/list');
INSERT INTO tb_sys_menu (id, parent_id, name, code, type, path) VALUES(33, 3, '资产风险', 'asset:risk', 'menu', '/asset/risk');

# 角色菜单
INSERT INTO tb_sys_role_menu (id, role_id, menu_id) VALUES(1, 2, 1);
INSERT INTO tb_sys_role_menu (id, role_id, menu_id) VALUES(2, 2, 2);
INSERT INTO tb_sys_role_menu (id, role_id, menu_id) VALUES(3, 2, 21);
INSERT INTO tb_sys_role_menu (id, role_id, menu_id) VALUES(4, 2, 22);
