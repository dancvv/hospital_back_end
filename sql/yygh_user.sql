Drop database if exists `yygh_user`;
Create database yygh_cmn;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `certificates_type` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型',
  `certificates_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件编号',
  `sex` tinyint(3) NULL DEFAULT NULL COMMENT '性别',
  `birthdate` date NULL DEFAULT NULL COMMENT '出生年月',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `is_marry` tinyint(3) NULL DEFAULT NULL COMMENT '是否结婚',
  `province_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省code',
  `city_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市code',
  `district_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区code',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详情地址',
  `contacts_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `contacts_certificates_type` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人证件类型',
  `contacts_certificates_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人证件号',
  `contacts_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人手机',
  `card_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就诊卡号',
  `is_insure` tinyint(3) NULL DEFAULT 0 COMMENT '是否有医保',
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '状态（0：默认 1：已认证）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '就诊人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of patient
-- ----------------------------
INSERT INTO `patient` VALUES (1, 18, '老王', '10', '35058212223123', 1, '2009-05-01', '1391121232', 0, '130000', '130800', '130802', '王家村97号', '13922212312', '10', '23214312412412312312', '13782321231', NULL, 0, 1, '2021-09-06 16:58:37', '2021-09-06 17:07:52', 0);
INSERT INTO `patient` VALUES (8, 18, '刘多鱼', '10', '35121292382222', 1, '2020-01-26', '13002123567', 0, '110000', '110100', '110102', '多余镇23号', '', '', '', '', NULL, 0, 0, '2021-09-06 17:18:33', '2021-09-06 17:18:33', 0);
INSERT INTO `patient` VALUES (9, 18, '张三', '10', '3441273718231212', 1, '2021-09-05', '13901212003', 0, '110000', '110100', '110101', '张村24号', '', '', '', '', NULL, 0, 0, '2021-09-06 17:23:20', '2021-09-06 21:03:27', 1);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `openid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信openid',
  `nick_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `certificates_type` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型',
  `certificates_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件编号',
  `certificates_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件路径',
  `auth_status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '认证状态（0：未认证 1：认证中 2：认证成功 -1：认证失败）',
  `status` tinyint(3) NOT NULL DEFAULT 1 COMMENT '状态（0：锁定 1：正常）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uk_mobile`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (10, NULL, NULL, '13002918045', '', NULL, NULL, NULL, 0, 1, '2021-09-01 11:57:15', '2021-09-03 16:30:00', 1);
INSERT INTO `user_info` VALUES (11, NULL, NULL, '13002910804', '', NULL, NULL, NULL, 0, 1, '2021-09-01 18:03:57', '2021-09-01 18:03:57', 0);
INSERT INTO `user_info` VALUES (12, NULL, NULL, '13022424222', '', NULL, NULL, NULL, 0, 1, '2021-09-02 10:50:34', '2021-09-02 10:50:34', 0);
INSERT INTO `user_info` VALUES (17, NULL, NULL, '13901121004', '', NULL, NULL, NULL, 0, 1, '2021-09-03 17:26:10', '2021-09-03 17:29:26', 1);
INSERT INTO `user_info` VALUES (18, 'o3_SC5zzuEDMhp3NAfPVkpFFUNFI', 'PENG', '13901121004', '张小宝', '身份证', '3402131222321122', 'https://yygh-xzp.oss-cn-beijing.aliyuncs.com/2021/09/04/3c760e1ade504381a538fb8b8022c70awallhaven-pkjxep.jpg', 2, 1, '2021-09-03 17:27:05', '2021-09-08 09:25:30', 0);
INSERT INTO `user_info` VALUES (19, NULL, NULL, '13002918045', '', NULL, NULL, NULL, 0, 1, '2021-09-06 16:46:01', '2021-09-06 16:46:01', 0);

-- ----------------------------
-- Table structure for user_login_record
-- ----------------------------
DROP TABLE IF EXISTS `user_login_record`;
CREATE TABLE `user_login_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户登录记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_login_record
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
