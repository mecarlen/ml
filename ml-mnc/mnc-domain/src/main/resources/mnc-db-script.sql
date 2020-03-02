/* 管理端系列表 */
CREATE TABLE adm_app (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  pl_code varchar(50) NOT NULL COMMENT '产品线编码',
  pl_name varchar(50) NOT NULL COMMENT '产品线名称',
  app_code varchar(50) NOT NULL COMMENT '应用编码',
  app_name varchar(100) NOT NULL COMMENT '应用名称',
  owner_erp varchar(50) NOT NULL COMMENT '负责人ERP',
  owner_name varchar(50) NOT NULL COMMENT '负责人姓名',
  dingding varchar(500) NOT NULL COMMENT '钉钉告警url',
  member_mobiles varchar(600) COMMENT '应用成员手机号',
  member_emails varchar(1000) COMMENT '应用成员邮箱',
  alarm_status tinyint(1) NOT NULL DEFAULT 0 COMMENT '告警开启状态,0-关闭,1-开启',
  need_callback tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否需要回传推送结果,0-否,1-是',
  remark varchar(600) DEFAULT '' COMMENT '备注',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_adm_app PRIMARY KEY (id),
  CONSTRAINT uk_adm_app_ac UNIQUE KEY(app_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '应用表信息表';

CREATE TABLE adm_msg_queue (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  app_id int(11) NOT NULL COMMENT '产品线编码',
  pl_code varchar(50) NOT NULL COMMENT '产品线编码',
  app_code varchar(50) NOT NULL COMMENT '应用编码',
  queue_name varchar(50) NOT NULL COMMENT '队列名称',
  route_key varchar(50) NOT NULL COMMENT '路由key',
  max_retry_count tinyint(2) NOT NULL COMMENT '最大重试次数',
  retry_intervals varchar(100) NOT NULL COMMENT '重试间格,多值以英文逗号隔开，支持s-秒,m-分,h-小时',
  need_callback tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否需要回传推送结果,0-否,1-是',
  remark varchar(600) DEFAULT '' COMMENT '备注',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_adm_msg_queue PRIMARY KEY (id)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '消息队列信息表';

CREATE TABLE adm_send_url_config(
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  app_id int(11) NOT NULL COMMENT '应用id',
  app_code varchar(50) NOT NULL COMMENT '应用编码',
  queue_id int(11) NOT NULL DEFAULT 0 COMMENT '队列id,默认：0-代表应用全局',
  queue_name varchar(50) NOT NULL COMMENT '队列名称',
  route_key varchar(50) NOT NULL COMMENT '路由key',
  vcc_id int(11) NOT NULL DEFAULT 0 COMMENT '企业id',
  business_type varchar(50) NOT NULL DEFAULT '' COMMENT '业务类型',
  url_address varchar(200) NOT NULL COMMENT 'http推送url',
  success_field_code varchar(512) COMMENT '成功字段与编码,key=value格式,key支持多层,字段名以“.”分隔,如：status=200或data.status=200',
  auth_field varchar(512) COMMENT '认证信息字段,支持多层,字段名以“.”分隔,如:token,data.token',
  auth_method tinyint(1) NOT NULL DEFAULT 0 COMMENT '认证方式，0-header方式,1-body方式,2-queryString方式',
  remark varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_callback_msg PRIMARY KEY (id),
  CONSTRAINT uk_adm_send_url_config_url UNIQUE KEY(url_address)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '推送URL配置表';

CREATE TABLE adm_auth_renewal_config(
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  app_code varchar(50) NOT NULL COMMENT '应用编码',
  queue_name varchar(50) NOT NULL COMMENT '队列名称',
  route_key varchar(50) NOT NULL COMMENT '路由key',
  vcc_id int(11) NOT NULL DEFAULT 0 COMMENT '企业id',
  business_type varchar(50) NOT NULL DEFAULT '' COMMENT '业务类型',
  send_url_id int(11) COMMENT '推送URL id',
  send_url_address varchar(500) NOT NULL COMMENT 'http推送url，不包含参数',
  renewal_url_address varchar(500) NOT NULL COMMENT '续签URL',
  auth_data_field varchar(100) NOT NULL COMMENT '获取认证信息字段名多层以“.”号连接，如data.token',
  remark varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_callback_msg PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '认证续签配置表';

/* 钉钉消息表 */
CREATE TABLE dingding_msg (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  app_id int(11) COMMENT '应用id',
  pl_code varchar(50) COMMENT '产品线编码',
  app_code varchar(50) NOT NULL COMMENT '应用编码',
  queue_name varchar(50) NOT NULL COMMENT '队列名称',
  route_key varchar(50) NOT NULL COMMENT '路由key',
  vcc_id int(11) NOT NULL DEFAULT 0 COMMENT '企业id',
  business_type varchar(50) NOT NULL DEFAULT '' COMMENT '业务类型',
  business_id varchar(50) NOT NULL COMMENT '业务id',
  msg_title varchar(150) NOT NULL COMMENT '消息头',
  msg_content varchar(4096) NOT NULL COMMENT '消息体',
  business_accept_time_mills bigint (15) COMMENT '业务接收时间，默认等于MNC接受时间',
  dingding_url varchar(500) NOT NULL COMMENT '推送钉钉url',
  specify_send_time_mills bigint (15) COMMENT '业务指定推送时间毫秒数',
  retry_interval varchar(256) COMMENT '业务指定推送间隔时间',
  accept_time_mills bigint (15) NOT NULL COMMENT 'MNC接收消息时间',
  first_send_time_mills bigint (15) COMMENT '首次推送时间',
  next_send_time_mills bigint (15) COMMENT '待推送时间毫秒数',
  first_success_send_time_mills bigint (15) COMMENT '首次电成功推送时间',
  retry_count tinyint(2) NOT NULL DEFAULT 0 COMMENT '已重试次数',
  send_status tinyint(1) NOT NULL DEFAULT 0 COMMENT '推送状态',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_dingding_msg PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '钉钉消息表';
alter table dingding_msg add index idx_dingding_msg_bid(business_id);
alter table dingding_msg add index idx_dingding_msg_tst(next_send_time_mills);

CREATE TABLE dingding_msg_send_record (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  business_uid varchar(200) NOT NULL COMMENT '业务uid',
  begin_time_mills bigint (15) NOT NULL COMMENT '开始时间',
  end_time_mills bigint (15) NOT NULL COMMENT '结束时间',
  send_status tinyint(1) NOT NULL COMMENT '推送状态',
  send_type tinyint(1) NOT NULL DEFAULT 0 COMMENT '推送类型,0-自动,1-手动',
  sender_erp varchar(50) NOT NULL DEFAULT 'MNC' COMMENT '推送人ERP,MNC-MNC即时推送,USC-USC定时重试,邮箱前缀-手动推送人邮箱前缀',
  sender_name varchar(100) NOT NULL DEFAULT 'MNC' COMMENT '推送人姓名',
  remark varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_dingding_msg_send_record PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '钉钉消息推送记录表';
alter table dingding_msg_send_record add index idx_dingding_msg_send_record_buid(business_uid);

CREATE TABLE dingding_callback_msg (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  app_id int(11) NOT NULL COMMENT '应用id',
  pl_code varchar(50) NOT NULL COMMENT '产品线编码',
  app_code varchar(50) NOT NULL COMMENT '应用编码',
  queue_name varchar(50) NOT NULL COMMENT '队列名称',
  route_key varchar(50) NOT NULL COMMENT '路由key',
  vcc_id int(11) NOT NULL DEFAULT 0 COMMENT '企业id',
  business_type varchar(50) NOT NULL DEFAULT '' COMMENT '业务类型',
  business_id varchar(50) NOT NULL COMMENT '业务id',
  return_json varchar(500) NOT NULL COMMENT '接口返回JSON',
  msg_content varchar(4096) NOT NULL COMMENT '消息体json',
  send_status tinyint(1) NOT NULL DEFAULT 0 COMMENT '推送状态',
  retry_count tinyint(2) NOT NULL DEFAULT 0 COMMENT '已重试次数',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_callback_msg PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '钉钉回调消息表';
alter table dingding_callback_msg add index idx_dingding_callback_msg_bid(business_id);

/* http消息表 */
CREATE TABLE http_msg (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  app_id int(11) COMMENT '应用id',
  pl_code varchar(50) COMMENT '产品线编码',
  app_code varchar(50) NOT NULL COMMENT '应用编码',
  queue_name varchar(50) NOT NULL COMMENT '队列名称',
  route_key varchar(50) NOT NULL COMMENT '路由key',
  vcc_id int(11) NOT NULL DEFAULT 0 COMMENT '企业id',
  business_type varchar(50) NOT NULL DEFAULT '' COMMENT '业务类型',
  business_id varchar(50) NOT NULL COMMENT '业务id',
  msg_content varchar(4096) NOT NULL COMMENT '消息体',
  business_accept_time_mills bigint (15) COMMENT '业务接收时间，默认等于MNC接受时间',
  send_url varchar(2048) NOT NULL COMMENT 'http推送url',
  auth_json varchar(512) COMMENT '认证参数',
  auth_expire_time_mills bigint (15) COMMENT '认证参数过期毫秒数',
  success_field_code varchar(512) COMMENT '成功字段与编码,key=value格式,key支持多层,字段名以“.”分隔,如：status=200或data.status=200',
  specify_send_time_mills bigint (15) COMMENT '业务指定推送时间毫秒数',
  retry_interval varchar(256) COMMENT '业务指定推送间隔时间',
  accept_time_mills bigint (15) NOT NULL COMMENT 'MNC接收消息时间',
  first_send_time_mills bigint (15) COMMENT '首次推送时间',
  next_send_time_mills bigint (15) COMMENT '待推送时间毫秒数',
  first_success_send_time_mills bigint (15) COMMENT '首次电成功推送时间',
  retry_count tinyint(2) NOT NULL DEFAULT 0 COMMENT '已重试次数,含自动重试次数',
  auto_retry_count tinyint(1) NOT NULL DEFAULT 0 COMMENT '自动重试次数',
  send_status tinyint(1) NOT NULL DEFAULT 0 COMMENT '推送状态,-1-失败,0-初始,1-推送中,2-成功',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_dingding_msg PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '钉钉消息表';
alter table http_msg add index idx_http_msg_bid(business_id);
alter table http_msg add index idx_http_msg_nstm(next_send_time_mills);

CREATE TABLE http_msg_send_record (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  business_uid varchar(200) NOT NULL COMMENT '业务uid',
  begin_time_mills bigint (15) NOT NULL COMMENT '开始时间',
  end_time_mills bigint (15) NOT NULL COMMENT '结束时间',
  send_status tinyint(1) NOT NULL COMMENT '推送状态',
  send_type tinyint(1) NOT NULL DEFAULT 0 COMMENT '推送类型,0-自动,1-手动',
  sender_erp varchar(50) NOT NULL DEFAULT 'MNC' COMMENT '推送人ERP,MNC-MNC即时推送,USC-USC定时重试,邮箱前缀-手动推送人邮箱前缀',
  sender_name varchar(100) NOT NULL DEFAULT 'MNC' COMMENT '推送人姓名',
  remark varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_http_msg_send_record PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'http消息推送记录表';
alter table http_msg_send_record add index idx_http_msg_send_record_buid(business_uid);

CREATE TABLE http_callback_msg (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  app_id int(11) NOT NULL COMMENT '应用id',
  pl_code varchar(50) NOT NULL COMMENT '产品线编码',
  app_code varchar(50) NOT NULL COMMENT '应用编码',
  queue_name varchar(50) NOT NULL COMMENT '队列名称',
  route_key varchar(50) NOT NULL COMMENT '路由key',
  vcc_id int(11) NOT NULL DEFAULT 0 COMMENT '企业id',
  business_type varchar(50) NOT NULL DEFAULT '' COMMENT '业务类型',
  send_url varchar(2048) NOT NULL COMMENT 'http推送url',
  business_id varchar(50) NOT NULL COMMENT '业务id',
  return_json varchar(500) NOT NULL COMMENT '接口返回JSON',
  msg_content varchar(4096) NOT NULL COMMENT '消息体json',
  send_status tinyint(1) NOT NULL DEFAULT 0 COMMENT '推送状态',
  retry_count tinyint(2) NOT NULL DEFAULT 0 COMMENT '已重试次数',
  yn tinyint(1) DEFAULT 1 NOT NULL COMMENT '物理状态,-1-删除,0-停用,1-在用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  CONSTRAINT pk_callback_msg PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'http回调消息表';
alter table http_callback_msg add index idx_http_callback_msg_bid(business_id);
