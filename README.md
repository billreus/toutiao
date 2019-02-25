# 数据库

## SQL

### 用户(User)

字段名 | 数据类型 | 含义
---------|----------|---------
 id | int(11) | id
 name | varchar(64) | 用户名
 password | varchar(128) | 密码
 salt | varchar(32) | salt加密
 head_url | varchar(256) | 头像链接

 ### 登陆保存时长(login_ticket)

 字段名 | 数据类型 | 含义
---------|----------|---------
 id | int | id
 user_id | int | 用户名对应id
 ticket | varchar(45) | ticket标记
 expired | DATETIME | 过期时间
 status | int | 有效无效（用于注销）

### 资讯(News)

 字段名 | 数据类型 | 含义
---------|----------|---------
 id | B1 | 
 title | B2 | 资讯标题
 link | B3 | 图片链接
 image | varchar(256) | 资讯图片保存地址
 like_count | int | 点赞数
 comment_count | int | 评论数
 user_id | int(11) | 归属用户
 created_date | datetime | 创建日期

  ### 评论(Comment)

 字段名 | 数据类型 | 含义
---------|----------|---------
 id |  | 
 content |  | 评论内容
 user_id | B3 | 关联用户
 entity_id | | 评论资讯newId/评论评论commentId
 entity_type | | new/comment
 created_date |  | 创建日期
 status |  | 有效无效

 ### 站内信(Message)

 字段名 | 数据类型 | 含义
---------|----------|---------
 id | B1 | C1
 fromid | B2 | C2
 toid | B3 | C3
 content |  |
 conversation_id |  |
 created_date | |



 ## MyBatis

 执行SQL获取数据，通过注解配置@Mapper或者XML配置