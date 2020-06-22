CREATE TABLE IF NOT EXISTS `call_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `app_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户标识',
  `ability_uri` varchar(64) NOT NULL DEFAULT '' COMMENT '调用地址',
  `create_time` datetime NOT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口调用记录';


CREATE TABLE IF NOT EXISTS `call_success_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `app_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户标识',
  `ability_uri` varchar(64) NOT NULL DEFAULT '' COMMENT '调用地址',
  `params` varchar(128) NOT NULL DEFAULT '' COMMENT '请求参数',
  `create_time` datetime NOT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='接口调用成功记录';


