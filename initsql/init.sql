-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '用户id'
        primary key,
    username     varchar(256) default 'tianlin'         not null comment '用户昵称',
    userAccount  varchar(256)                           null comment '用户账号',
    avatarUrl    varchar(2048)                          null comment '头像',
    gender       tinyint                                null comment '性别',
    userPassword varchar(512)                           not null comment '密码',
    phone        varchar(128)                           null comment '电话',
    email        varchar(512)                           null comment '邮箱',
    userStatus   int          default 0                 null comment '用户状态 0正常',
    createTime   datetime     default CURRENT_TIMESTAMP null comment '数据插入时间',
    updateTime   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '数据更新时间',
    isDelete     tinyint      default 0                 not null comment '逻辑删除(0未删除 1删除)',
    userRole     int          default 0                 not null comment '用户角色 0 普通用户 1管理员',
    userCode     varchar(512)                           not null comment '用户编码',
    tags         varchar(1024)                          null comment '用户标签列表'
)
    comment '用户表';

