# 数据库

## SQL

### 用户(User)

字段名 | 数据类型 | 含义
---------|----------|---------
 id | B1 | id
 name | B2 | C2
 password | B3 | C3
 salt |  |
 head_url |  |

 ### 站内信(Message)

 字段名 | 数据类型 | 含义
---------|----------|---------
 id | B1 | C1
 fromid | B2 | C2
 toid | B3 | C3
 content |  |
 conversation_id |  |
 created_date | |

### 资讯(News)

 字段名 | 数据类型 | 含义
---------|----------|---------
 id | B1 | C1
 title | B2 | C2
 link | B3 | C3
 image |  |
 like_count |  |
 comment_count | |
 user_id | |
 created_date | |

 ### 评论(Comment)

 字段名 | 数据类型 | 含义
---------|----------|---------
 id | B1 | C1
 content | B2 | C2
 user_id | B3 | C3
 created_date |  |
 news_id |  |

 ## MyBatis

 执行SQL获取数据，通过注解配置或者XML配置