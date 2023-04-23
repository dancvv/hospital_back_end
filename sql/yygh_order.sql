Drop database if exists `yygh_order`;
Create database yygh_cmn;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NULL DEFAULT NULL,
  `out_trade_no` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单交易号',
  `hoscode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院编号',
  `hosname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医院名称',
  `depcode` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科室编号',
  `depname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科室名称',
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医生职称',
  `hos_schedule_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排班编号（医院自己的排班主键）',
  `reserve_date` date NULL DEFAULT NULL COMMENT '安排日期',
  `reserve_time` tinyint(3) NULL DEFAULT 0 COMMENT '安排时间（0：上午 1：下午）',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '就诊人id',
  `patient_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就诊人名称',
  `patient_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '就诊人手机',
  `hos_record_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预约记录唯一标识（医院预约记录主键）',
  `number` int(11) NULL DEFAULT NULL COMMENT '预约号序',
  `fetch_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '建议取号时间',
  `fetch_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取号地点',
  `amount` decimal(10, 0) NULL DEFAULT NULL COMMENT '医事服务费',
  `quit_time` datetime NULL DEFAULT NULL COMMENT '退号时间',
  `order_status` tinyint(3) NULL DEFAULT NULL COMMENT '订单状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_hoscode`(`hoscode`) USING BTREE,
  INDEX `idx_hos_schedule_id`(`hos_schedule_id`) USING BTREE,
  INDEX `idx_hos_record_id`(`hos_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (44, 18, '163176251438438', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f33', '2021-09-17', 0, 1, '老王', '1391121232', '23', 19, '2021-09-1909:00前', '一层114窗口', 100, '2021-09-19 22:30:00', -1, '2021-09-16 11:21:54', '2021-09-17 16:21:12', 0);
INSERT INTO `order_info` VALUES (45, 18, '163176359809455', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f33', '2021-09-17', 0, 8, '刘多鱼', '13002123567', '24', 20, '2021-09-1909:00前', '一层114窗口', 100, '2021-09-19 22:30:00', 1, '2021-09-16 11:39:58', '2021-09-17 16:21:17', 0);
INSERT INTO `order_info` VALUES (46, 18, '163176467635373', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f33', '2021-09-17', 0, 8, '刘多鱼', '13002123567', '25', 21, '2021-09-1909:00前', '一层114窗口', 100, '2021-09-19 22:30:00', -1, '2021-09-16 11:57:56', '2021-09-17 16:21:20', 0);
INSERT INTO `order_info` VALUES (47, 18, '16317649561550', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f33', '2021-09-17', 0, 1, '老王', '1391121232', '26', 22, '2021-09-1809:00前', '一层114窗口', 100, '2021-09-18 22:30:00', 1, '2021-09-16 12:02:36', '2021-09-17 16:18:07', 0);
INSERT INTO `order_info` VALUES (48, 18, '163186360310912', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f2d', '2021-09-19', 1, 1, '老王', '1391121232', '27', 12, '2021-09-1909:00前', '一层114窗口', 100, '2021-09-18 15:30:00', -1, '2021-09-17 15:26:43', '2021-09-17 15:26:43', 0);
INSERT INTO `order_info` VALUES (49, 18, '163186364254662', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f33', '2021-09-18', 0, 1, '老王', '1391121232', '28', 13, '2021-09-1809:00前', '一层114窗口', 100, '2021-09-19 15:30:00', -1, '2021-09-17 15:27:22', '2021-09-17 16:21:25', 0);
INSERT INTO `order_info` VALUES (50, 18, '16318639780213', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f2d', '2021-09-19', 1, 1, '老王', '1391121232', '29', 14, '2021-09-1909:00前', '一层114窗口', 100, '2021-09-18 15:30:00', -1, '2021-09-17 15:32:58', '2021-09-17 15:32:58', 0);
INSERT INTO `order_info` VALUES (51, 18, '163186466585521', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f33', '2021-09-18', 0, 8, '刘多鱼', '13002123567', '30', 12, '2021-09-1809:00前', '一层114窗口', 100, '2021-09-19 15:30:00', -1, '2021-09-17 15:44:25', '2021-09-17 16:21:28', 0);
INSERT INTO `order_info` VALUES (52, 18, '163186489433148', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f33', '2021-09-18', 0, 1, '老王', '1391121232', '31', 13, '2021-09-1809:00前', '一层114窗口', 100, '2021-09-18 15:30:00', -1, '2021-09-17 15:48:14', '2021-09-17 15:59:17', 0);
INSERT INTO `order_info` VALUES (53, 18, '163187005818971', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f2d', '2021-09-19', 1, 1, '老王', '1391121232', '32', 15, '2021-09-1909:00前', '一层114窗口', 100, '2021-09-19 15:30:00', -1, '2021-09-17 17:14:18', '2021-09-17 17:15:48', 0);
INSERT INTO `order_info` VALUES (54, 18, '163187031618789', '1000_0', '北京协和医院', '200040878', '多发性硬化专科门诊', '医师', '61263a20e6a10867618d2f2d', '2021-09-19', 1, 1, '老王', '1391121232', '33', 16, '2021-09-1909:00前', '一层114窗口', 100, '2021-09-18 15:30:00', -1, '2021-09-17 17:18:36', '2021-09-17 17:18:36', 0);

-- ----------------------------
-- Table structure for payment_info
-- ----------------------------
DROP TABLE IF EXISTS `payment_info`;
CREATE TABLE `payment_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对外业务编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `payment_type` tinyint(1) NULL DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `subject` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `payment_status` tinyint(3) NULL DEFAULT NULL COMMENT '支付状态',
  `callback_time` datetime NULL DEFAULT NULL COMMENT '回调时间',
  `callback_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_info
-- ----------------------------
INSERT INTO `payment_info` VALUES (12, '163176251438438', 44, 2, '4200001180202109160006335634', 100.00, '2021-09-17|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-09-16 11:33:48', '{transaction_id=4200001180202109160006335634, nonce_str=BhUIJbHnMO2oaIK5, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuIPUocVUSaVizTW6j29stAU, sign=777070884BD2D9B51633DB9F360A040E, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=163176251438438, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20210916112233, is_subscribe=N, return_code=SUCCESS}', '2021-09-16 11:22:17', '2021-09-16 11:33:47', 0);
INSERT INTO `payment_info` VALUES (13, '163176359809455', 45, 2, '4200001151202109162329228445', 100.00, '2021-09-17|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-09-16 11:40:13', '{transaction_id=4200001151202109162329228445, nonce_str=NF6WoCGrE3ZYk46x, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuIPUocVUSaVizTW6j29stAU, sign=1BD18E6834CA172AC070F1A7C0A92D92, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=163176359809455, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20210916114011, is_subscribe=N, return_code=SUCCESS}', '2021-09-16 11:40:03', '2021-09-16 11:40:13', 0);
INSERT INTO `payment_info` VALUES (14, '163176467635373', 46, 2, '4200001173202109167525767976', 100.00, '2021-09-17|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-09-16 11:58:06', '{transaction_id=4200001173202109167525767976, nonce_str=KdfBuu3hlX6H4kE9, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuIPUocVUSaVizTW6j29stAU, sign=F0BF58BBC4B12B55136908A23DD74DF4, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=163176467635373, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20210916115803, is_subscribe=N, return_code=SUCCESS}', '2021-09-16 11:57:59', '2021-09-16 11:58:06', 0);
INSERT INTO `payment_info` VALUES (15, '16317649561550', 47, 2, '4200001160202109166702845237', 100.00, '2021-09-17|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-09-16 12:03:32', '{transaction_id=4200001160202109166702845237, nonce_str=5RHQI33I19b8GXeH, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuIPUocVUSaVizTW6j29stAU, sign=DDC83B70C2A2F6E70B386A60B4C94105, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=16317649561550, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20210916120330, is_subscribe=N, return_code=SUCCESS}', '2021-09-16 12:03:19', '2021-09-16 12:03:31', 0);
INSERT INTO `payment_info` VALUES (16, '163186489433148', 52, 2, '4200001170202109176910518092', 100.00, '2021-09-18|北京协和医院|多发性硬化专科门诊|医师', 2, '2021-09-17 15:58:42', '{transaction_id=4200001170202109176910518092, nonce_str=5oggIL55zrgtZk0s, trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuIPUocVUSaVizTW6j29stAU, sign=C25849EFF30519E4DB13701E148C4AAF, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=163186489433148, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20210917155837, is_subscribe=N, return_code=SUCCESS}', '2021-09-17 15:58:29', '2021-09-17 15:58:42', 0);

-- ----------------------------
-- Table structure for refund_info
-- ----------------------------
DROP TABLE IF EXISTS `refund_info`;
CREATE TABLE `refund_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对外业务编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单编号',
  `payment_type` tinyint(3) NULL DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `subject` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易内容',
  `refund_status` tinyint(3) NULL DEFAULT NULL COMMENT '退款状态',
  `callback_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调信息',
  `callback_time` datetime NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退款信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_info
-- ----------------------------
INSERT INTO `refund_info` VALUES (1, '163176467635373', 46, 2, '50300909572021091612544336951', 100.00, '2021-09-17|北京协和医院|多发性硬化专科门诊|医师', 2, '{\"transaction_id\":\"4200001173202109167525767976\",\"nonce_str\":\"xoReiDU9q3ogJj1J\",\"out_refund_no\":\"tk163176467635373\",\"sign\":\"8CD9225F5F3537D2F4A8B1E8863AB87E\",\"return_msg\":\"OK\",\"mch_id\":\"1558950191\",\"refund_id\":\"50300909572021091612544336951\",\"cash_fee\":\"1\",\"out_trade_no\":\"163176467635373\",\"coupon_refund_fee\":\"0\",\"refund_channel\":\"\",\"appid\":\"wx74862e0dfcf69954\",\"refund_fee\":\"1\",\"total_fee\":\"1\",\"result_code\":\"SUCCESS\",\"coupon_refund_count\":\"0\",\"cash_refund_fee\":\"1\",\"return_code\":\"SUCCESS\"}', '2021-09-16 21:49:57', '2021-09-16 21:49:56', '2021-09-16 21:49:57', 0);
INSERT INTO `refund_info` VALUES (2, '163176251438438', 44, 2, '50301409552021091612549672800', 100.00, '2021-09-17|北京协和医院|多发性硬化专科门诊|医师', 2, '{\"transaction_id\":\"4200001180202109160006335634\",\"nonce_str\":\"fLJPUAoUl2CJSSts\",\"out_refund_no\":\"tk163176251438438\",\"sign\":\"A9CAC30C7A7120B8B74AEB9ED70569FD\",\"return_msg\":\"OK\",\"mch_id\":\"1558950191\",\"refund_id\":\"50301409552021091612549672800\",\"cash_fee\":\"1\",\"out_trade_no\":\"163176251438438\",\"coupon_refund_fee\":\"0\",\"refund_channel\":\"\",\"appid\":\"wx74862e0dfcf69954\",\"refund_fee\":\"1\",\"total_fee\":\"1\",\"result_code\":\"SUCCESS\",\"coupon_refund_count\":\"0\",\"cash_refund_fee\":\"1\",\"return_code\":\"SUCCESS\"}', '2021-09-16 21:53:14', '2021-09-16 21:53:14', '2021-09-16 21:53:14', 0);
INSERT INTO `refund_info` VALUES (3, '163186489433148', 52, 2, '50300809292021091712553161800', 100.00, '2021-09-18|北京协和医院|多发性硬化专科门诊|医师', 2, '{\"transaction_id\":\"4200001170202109176910518092\",\"nonce_str\":\"1XaXA8s9jnl4Olgt\",\"out_refund_no\":\"tk163186489433148\",\"sign\":\"781BEF5BABF8B16A3314C4418560B192\",\"return_msg\":\"OK\",\"mch_id\":\"1558950191\",\"refund_id\":\"50300809292021091712553161800\",\"cash_fee\":\"1\",\"out_trade_no\":\"163186489433148\",\"coupon_refund_fee\":\"0\",\"refund_channel\":\"\",\"appid\":\"wx74862e0dfcf69954\",\"refund_fee\":\"1\",\"total_fee\":\"1\",\"result_code\":\"SUCCESS\",\"coupon_refund_count\":\"0\",\"cash_refund_fee\":\"1\",\"return_code\":\"SUCCESS\"}', '2021-09-17 15:59:49', '2021-09-17 15:59:48', '2021-09-17 15:59:49', 0);

SET FOREIGN_KEY_CHECKS = 1;
