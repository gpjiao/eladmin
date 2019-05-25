
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `path` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `pid` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `i_frame` bit(1) NULL DEFAULT NULL COMMENT '是否外链',
  `sort` bigint(20) NOT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '系统菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------

INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (100, '系统管理', 'system', 'system', NULL, 0, b'0', 1, '2018-12-18 15:11:29');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (100100, '代码生成', 'dev', 'generator', 'generator/index', 100, b'0', 2, '2019-01-11 15:45:55');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (100101, '用户管理', 'peoples', 'user', 'system/user/index', 100, b'0', 3, '2018-12-18 15:14:44');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (100102, '角色管理', 'role', 'role', 'system/role/index', 100, b'0', 4, '2018-12-18 15:16:07');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (100103, '权限管理', 'permission', 'permission', 'system/permission/index', 100, b'0', 5, '2018-12-18 15:16:45');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (100104, '菜单管理', 'menu', 'menu', 'system/menu/index', 100, b'0', 6, '2018-12-18 15:17:28');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (100105, '组织机构管理', 'org', 'org', 'system/organization/index', 100, b'0', 7, '2019-03-18 15:17:28');

INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (101, '系统监控', 'monitor', 'monitor', NULL, 0, b'0', 8, '2018-12-18 15:17:48');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (101100, '操作日志', 'log', 'logs', 'monitor/log/index', 101, b'0', 9, '2018-12-18 15:18:26');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (101101, '异常日志', 'error', 'errorLog', 'monitor/log/errorLog', 101, b'0', 10, '2019-01-13 13:49:03');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (101102, '系统缓存', 'redis', 'redis', 'monitor/redis/index', 101, b'0', 11, '2018-12-18 15:19:01');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (101103, 'SQL监控', 'sqlMonitor', 'http://api.auauz.net/druid', NULL, 101, b'1', 12, '2018-12-18 15:19:34');

INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (102, '三方工具', 'tools', 'tools', '', 0, b'0', 13, '2018-12-27 10:11:26');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (102100, '邮件工具', 'email', 'email', 'tools/email/index', 102, b'0', 14, '2018-12-27 10:13:09');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (102101, 'SM.MS图床', 'image', 'pictures', 'tools/picture/index', 102, b'0', 15, '2018-12-28 09:36:53');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (102102, '七牛云存储', 'qiniu', 'qiniu', 'tools/qiniu/index', 102, b'0', 16, '2018-12-31 11:12:15');

INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (103, '组件管理', 'zujian', 'components', NULL, 0, b'0', 17, '2018-12-19 13:38:16');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (103100, '图标库', 'icon', 'icon', 'components/IconSelect', 103, b'0', 18, '2018-12-19 13:38:49');
INSERT INTO `sys_menu`(`id`, `name`, `icon`, `path`, `component`, `pid`, `i_frame`, `sort`, `create_time`) VALUES (103101, '富文本', 'fwb', 'tinymce', 'components/Editor', 103, b'0', 19, '2018-12-27 11:58:25');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `alias` varchar(255) DEFAULT NULL COMMENT '别名',
  `pid` bigint(20) NOT NULL COMMENT '上级权限',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '系统权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (1, 'ADMIN', '超级管理员', 0, '2018-12-03 12:27:48');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (2, 'USER_ALL', '用户管理', 0, '2018-12-03 12:28:19');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (3, 'USER_SELECT', '用户查询', 2, '2018-12-03 12:31:35');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (4, 'USER_CREATE', '用户创建', 2, '2018-12-03 12:31:35');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (5, 'USER_EDIT', '用户编辑', 2, '2018-12-03 12:31:35');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (6, 'USER_DELETE', '用户删除', 2, '2018-12-03 12:31:35');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (7, 'ROLES_ALL', '角色管理', 0, '2018-12-03 12:28:19');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (8, 'ROLES_SELECT', '角色查询', 7, '2018-12-03 12:31:35');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (10, 'ROLES_CREATE', '角色创建', 7, '2018-12-09 20:10:16');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (11, 'ROLES_EDIT', '角色编辑', 7, '2018-12-09 20:10:42');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (12, 'ROLES_DELETE', '角色删除', 7, '2018-12-09 20:11:07');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (13, 'PERMISSION_ALL', '权限管理', 0, '2018-12-09 20:11:37');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (14, 'PERMISSION_SELECT', '权限查询', 13, '2018-12-09 20:11:55');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (15, 'PERMISSION_CREATE', '权限创建', 13, '2018-12-09 20:14:10');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (16, 'PERMISSION_EDIT', '权限编辑', 13, '2018-12-09 20:15:44');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (17, 'PERMISSION_DELETE', '权限删除', 13, '2018-12-09 20:15:59');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (18, 'REDIS_ALL', '缓存管理', 0, '2018-12-17 13:53:25');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (19, 'REDIS_CREATE', '新增缓存', 18, '2018-12-17 13:53:44');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (20, 'REDIS_SELECT', '缓存查询', 18, '2018-12-17 13:54:07');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (21, 'REDIS_EDIT', '缓存编辑', 18, '2018-12-17 13:54:26');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (22, 'REDIS_DELETE', '缓存删除', 18, '2018-12-17 13:55:04');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (23, 'PICTURE_ALL', '图床管理', 0, '2018-12-27 20:31:49');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (24, 'PICTURE_SELECT', '查询图片', 23, '2018-12-27 20:32:04');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (25, 'PICTURE_UPLOAD', '上传图片', 23, '2018-12-27 20:32:24');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (26, 'PICTURE_DELETE', '删除图片', 23, '2018-12-27 20:32:45');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (29, 'MENU_ALL', '菜单管理', 0, '2018-12-28 17:34:31');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (30, 'MENU_SELECT', '菜单查询', 29, '2018-12-28 17:34:41');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (31, 'MENU_CREATE', '菜单创建', 29, '2018-12-28 17:34:52');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (32, 'MENU_EDIT', '菜单编辑', 29, '2018-12-28 17:35:20');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (33, 'MENU_DELETE', '菜单删除', 29, '2018-12-28 17:35:29');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (34, 'ORGANIZATION_ALL', '组织机构', 0, '2019-03-19 17:34:31');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (35, 'ORGANIZATION_SELECT', '机构查询', 34, '2019-03-19 17:34:41');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (36, 'ORGANIZATION_CREATE', '机构创建', 34, '2019-03-19 17:34:52');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (37, 'ORGANIZATION_EDIT', '机构编辑', 34, '2019-03-19 17:35:20');
INSERT INTO `sys_permission`(`id`, `name`, `alias`, `pid`, `create_time`) VALUES (38, 'ORGANIZATION_DELETE', '机构删除', 34, '2019-03-19 17:35:29');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '系统角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `sys_role`(`id`, `name`, `remark`, `create_time`) VALUES (1, '管理员', '系统所有权', '2018-11-23 11:04:37');
INSERT INTO `sys_role`(`id`, `name`, `remark`, `create_time`) VALUES (2, '普通用户', '用于测试菜单与权限', '2018-11-23 13:09:06');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织机构id',
  `last_password_reset_time` datetime NULL DEFAULT NULL COMMENT '最后修改密码的日期',
  `enabled` bigint(20) NULL DEFAULT NULL COMMENT '状态：1启用、0禁用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_permission_id`(`email`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`(`id`, `username`, `password`, `avatar`, `email`, `org_id`, `last_password_reset_time`, `enabled`, `create_time`) VALUES (1, 'admin', '14e1b600b1fd579f47433b88e8d85291', 'https://i.loli.net/2019/01/16/5c3ed609e6f99.jpg', 'zhengjie@tom.com', 0, '2019-01-17 09:53:21', 1, '2018-08-23 09:11:56');
INSERT INTO `sys_user`(`id`, `username`, `password`, `avatar`, `email`, `org_id`, `last_password_reset_time`, `enabled`, `create_time`) VALUES (3, 'test', '14e1b600b1fd579f47433b88e8d85291', 'https://i.loli.net/2018/12/30/5c2871d6aa101.jpg', 'test@qq.com', NULL, NULL, 1, '2018-12-27 20:05:26');

-- ----------------------------
-- Table structure for sys_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles_menus`;
CREATE TABLE `sys_roles_menus`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
  INDEX `i_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '角色菜单表';

-- ----------------------------
-- Records of sys_roles_menus
-- ----------------------------
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 100);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 100100);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 100101);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 100102);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 100103);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 100104);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 100105);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 101);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 101100);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 101101);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 101102);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 101103);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 102);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 102100);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 102101);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 102102);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 103);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 103100);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (1, 103101);

INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (2, 100);
INSERT INTO `sys_roles_menus`(`role_id`, `menu_id`) VALUES (2, 100100);

-- ----------------------------
-- Table structure for sys_roles_permissions
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles_permissions`;
CREATE TABLE `sys_roles_permissions`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE,
  INDEX `i_permission_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '角色权限表';

-- ----------------------------
-- Records of sys_roles_permissions
-- ----------------------------
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (1, 1);
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (2, 2);
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (2, 3);
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (2, 4);
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (2, 5);
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (2, 6);
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (2, 23);
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (2, 24);
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (2, 25);
INSERT INTO `sys_roles_permissions`(`role_id`, `permission_id`) VALUES (2, 26);

-- ----------------------------
-- Table structure for sys_users_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_users_roles`;
CREATE TABLE `sys_users_roles`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `i_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '用户角色表';

-- ----------------------------
-- Records of sys_users_roles
-- ----------------------------
INSERT INTO `sys_users_roles`(`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `sys_users_roles`(`user_id`, `role_id`) VALUES (3, 2);

-- ----------------------------
-- Table structure for sys_email_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_email_config`;
CREATE TABLE `sys_email_config`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `from_user` varchar(255) DEFAULT NULL COMMENT '收件人',
  `host` varchar(255) DEFAULT NULL COMMENT '邮件服务器SMTP地址',
  `pass` varchar(255) DEFAULT NULL COMMENT '密码',
  `port` varchar(255) DEFAULT NULL COMMENT '端口',
  `user` varchar(255) DEFAULT NULL COMMENT '发件者用户名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '邮件代理配置表';


-- Table structure for sys_qiniu_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_qiniu_config`;
CREATE TABLE `sys_qiniu_config`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `access_key` text COMMENT 'accessKey',
  `bucket` varchar(255) DEFAULT NULL COMMENT 'Bucket 识别符',
  `host` varchar(255) NOT NULL COMMENT '外链域名',
  `secret_key` text COMMENT 'secretKey',
  `type` varchar(255) DEFAULT NULL COMMENT '空间类型',
  `zone` varchar(255) DEFAULT NULL COMMENT '机房',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '七牛云存储配置表';

-- ----------------------------
-- Table structure for sys_qiniu_content
-- ----------------------------
DROP TABLE IF EXISTS `sys_qiniu_content`;
CREATE TABLE `sys_qiniu_content`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `bucket` varchar(255) DEFAULT NULL COMMENT 'Bucket 识别符',
  `file_key` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `size` varchar(255) DEFAULT NULL COMMENT '文件大小',
  `type` varchar(255) DEFAULT NULL COMMENT '文件类型：私有或公开',
  `url` varchar(255) DEFAULT NULL COMMENT '文件url',
  `update_time` datetime NULL DEFAULT NULL COMMENT '上传或同步的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '七牛云存储数据表';

-- ----------------------------
-- Table structure for sys_picture
-- ----------------------------
DROP TABLE IF EXISTS `sys_picture`;
CREATE TABLE `sys_picture`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `filename` varchar(255) DEFAULT NULL COMMENT '图片名称',
  `size` bigint(20) DEFAULT NULL COMMENT '图片大小',
  `width` int(10) DEFAULT NULL COMMENT '图片宽度',
  `height` int(10) DEFAULT NULL COMMENT '图片高度',
  `url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `delete_url` varchar(255) DEFAULT NULL COMMENT '删除的URL',
  `create_time` datetime NULL DEFAULT NULL COMMENT '上传日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT 'SM.MS图床数据表';


-- ----------------------------
-- Table structure for sys_verification_code
-- ----------------------------
DROP TABLE IF EXISTS `sys_verification_code`;
CREATE TABLE `sys_verification_code`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `type` varchar(255) DEFAULT NULL COMMENT '验证码类型：email或者短信',
  `scenes` varchar(255) DEFAULT NULL COMMENT '业务名称：如重置邮箱、重置密码等',
  `value` varchar(255) DEFAULT NULL COMMENT '接收邮箱或者手机号码',
  `code` varchar(255) DEFAULT NULL COMMENT '验证码',
  `status` bit(1) NULL DEFAULT NULL COMMENT '状态：1有效、0过期',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '邮箱验证码表';

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `log_type` varchar(255) DEFAULT NULL,
  `request_ip` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `params` text,
  `exception_detail` text,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '系统日志表';

-- ----------------------------
-- Table structure for sys_visits
-- ----------------------------
DROP TABLE IF EXISTS `sys_visits`;
CREATE TABLE `sys_visits` (
  `id` bigint(20) NOT NULL,
  `date` varchar(255)  DEFAULT NULL,
  `week_day` varchar(255)  DEFAULT NULL,
  `ip_counts` bigint(20) DEFAULT NULL,
  `pv_counts` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '访问统计表';

-- ----------------------------
-- Table structure for sys_gen_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_gen_config`;
CREATE TABLE `sys_gen_config`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `cover` bit(1) NULL DEFAULT NULL COMMENT '是否覆盖',
  `module_name` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `pack` varchar(255) DEFAULT NULL COMMENT '至于哪个包下',
  `path` varchar(255) DEFAULT NULL COMMENT '前端代码生成的路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '代码生成配置表';

-- ----------------------------
-- Records of sys_gen_config
-- ----------------------------
INSERT INTO `sys_gen_config`(`id`, `author`, `cover`, `module_name`, `pack`, `path`) VALUES (1, 'jie', b'0', 'test', 'com.yunhan.modules', 'E:\\workspace\\eladmin-web');

SET FOREIGN_KEY_CHECKS = 1;