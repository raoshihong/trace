/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50722
Source Host           : 127.0.0.1:3306
Source Database       : trace

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-10-15 17:05:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for event
-- ----------------------------
DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '事件记录id',
  `span_id` varchar(200) DEFAULT NULL COMMENT '链路标识',
  `name` varchar(100) NOT NULL COMMENT '事件名称',
  `time` datetime NOT NULL COMMENT '事件发生时间',
  `type` varchar(100) DEFAULT NULL COMMENT '事件发生方式',
  PRIMARY KEY (`id`),
  KEY `idx_time` (`time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件记录总表';

-- ----------------------------
-- Table structure for event_content
-- ----------------------------
DROP TABLE IF EXISTS `event_content`;
CREATE TABLE `event_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '事件内容记录id',
  `span_id` varchar(200) NOT NULL COMMENT '链路标识',
  `table_name` varchar(200) DEFAULT NULL COMMENT '操作表名',
  `table_name_desc` varchar(200) DEFAULT NULL COMMENT '表名描述',
  `data_action_type` varchar(100) DEFAULT NULL COMMENT '数据操作类型update,insert,delete',
  `record_id` bigint(20) DEFAULT NULL COMMENT '操作表记录id',
  `before_body` text COMMENT '修改前的实体',
  `after_body` text COMMENT '修改后的实体',
  `data_id` bigint(20) DEFAULT NULL COMMENT '操作数据唯一标识id',
  `data_name` varchar(200) DEFAULT NULL COMMENT '操作数据标识名称',
  `data_table_name` varchar(200) DEFAULT NULL COMMENT '主表名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件操作内容记录表';

-- ----------------------------
-- Table structure for event_place
-- ----------------------------
DROP TABLE IF EXISTS `event_place`;
CREATE TABLE `event_place` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '事件发生地记录id',
  `span_id` varchar(200) DEFAULT NULL COMMENT '链路标识',
  `place_page_code` varchar(255) DEFAULT NULL COMMENT '操作页面代码',
  `place_page_name` varchar(255) DEFAULT NULL COMMENT '操作页面名称',
  `place_platform_code` varchar(255) DEFAULT NULL COMMENT '操作平台代码',
  `place_platform_name` varchar(255) DEFAULT NULL COMMENT '操作平台名称',
  `place_url` varchar(255) DEFAULT NULL COMMENT '操作资源URL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件发生地记录表';

-- ----------------------------
-- Table structure for event_user
-- ----------------------------
DROP TABLE IF EXISTS `event_user`;
CREATE TABLE `event_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '事件用户记录id',
  `span_id` varchar(200) DEFAULT NULL COMMENT '链路标识',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `user_mobile` varchar(255) DEFAULT NULL COMMENT '用户手机号',
  `user_origin` varchar(255) DEFAULT NULL COMMENT '用户来源',
  `app_id` int(11) DEFAULT NULL COMMENT '商户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件用户体系表';

-- ----------------------------
-- Table structure for table_config
-- ----------------------------
DROP TABLE IF EXISTS `table_config`;
CREATE TABLE `table_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(255) DEFAULT NULL COMMENT '表名称',
  `table_name_desc` varchar(255) DEFAULT NULL COMMENT '表描述',
  `app_id` varchar(255) DEFAULT NULL COMMENT '应用id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for table_property_config
-- ----------------------------
DROP TABLE IF EXISTS `table_property_config`;
CREATE TABLE `table_property_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `table_id` bigint(20) DEFAULT NULL,
  `primary_key` bit(1) DEFAULT b'0' COMMENT '是否主键',
  `foreign_key` bit(1) DEFAULT b'0' COMMENT '是否外键',
  `fKey_ref_property` varchar(255) DEFAULT NULL COMMENT '依赖外键名称',
  `fKey_ref_table_name` varchar(255) DEFAULT NULL COMMENT '依赖外键表名',
  `pro_en_name` varchar(255) DEFAULT NULL COMMENT '字段英文名',
  `pro_cn_name` varchar(255) DEFAULT NULL COMMENT '字段中文名',
  `encrypt` bit(1) DEFAULT b'0' COMMENT '是否加密字段',
  `type` varchar(255) DEFAULT NULL COMMENT '字段类型',
  `ref_origin_property` varchar(255) DEFAULT NULL COMMENT '加密字段对应的原字段',
  `data_type` varchar(255) DEFAULT NULL COMMENT '数据类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for trace_config
-- ----------------------------
DROP TABLE IF EXISTS `trace_config`;
CREATE TABLE `trace_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '事件名称',
  `page_code` varchar(255) DEFAULT NULL COMMENT '页面代码',
  `page_name` varchar(255) DEFAULT NULL COMMENT '页面名称',
  `platform_code` varchar(255) DEFAULT NULL COMMENT '平台代码',
  `platform_name` varchar(255) DEFAULT NULL COMMENT '平台名称',
  `type` varchar(255) DEFAULT NULL COMMENT '事件类型',
  `app_id` varchar(255) DEFAULT NULL COMMENT '应用id',
  `method` varchar(255) DEFAULT NULL COMMENT '请求方式',
  `url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `createAt` datetime DEFAULT NULL,
  `updateAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
