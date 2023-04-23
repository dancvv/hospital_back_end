Drop database if exists `yygh_hosp`;
Create database yygh_cmn;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hospital_set
-- ----------------------------
DROP TABLE IF EXISTS `hospital_set`;
CREATE TABLE `hospital_set`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `hosname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院名称',
  `hoscode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院编号',
  `api_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'api基础路径',
  `sign_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名秘钥',
  `contacts_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contacts_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人手机',
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_hoscode`(`hoscode`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '医院设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hospital_set
-- ----------------------------
INSERT INTO `hospital_set` VALUES (1, '北京协和医院', '1000_0', 'http://localhost:9998', 'acc13df8dc5fb4845c2a4c8d9da7fe11', '221', '13911231231', 1, '2021-08-10 20:57:47', '2021-09-13 19:47:13', 0);
INSERT INTO `hospital_set` VALUES (2, '北京积水潭医院1', '1000_1', 'http://localhost:10000/1', '1000_1', '111', '13512133211', 1, '2021-08-18 18:49:49', '2021-08-24 21:35:42', 0);
INSERT INTO `hospital_set` VALUES (3, '北京方正医院', '1000_2', 'http://localhost:10001/', '1000_2', '00', '12327832912', 1, '2021-08-20 19:49:28', '2021-08-24 21:35:44', 0);
INSERT INTO `hospital_set` VALUES (4, '北京抗日医院', '1000_3', 'http://localhost:10002/', '1000_3', '99', '13123123422', 1, '2021-08-20 19:50:12', '2021-08-24 21:35:47', 0);
INSERT INTO `hospital_set` VALUES (5, '北京省立医院', '1000_6', 'http://shengli.com', '1000_6', '32', '12435152', 1, '2021-08-21 17:26:09', '2021-08-24 21:35:51', 0);

SET FOREIGN_KEY_CHECKS = 1;
