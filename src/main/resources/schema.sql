-- 在 PostgreSQL 中创建 sys_user 表
CREATE TABLE IF NOT EXISTS sys_user (
    id SERIAL PRIMARY KEY,          -- 使用 SERIAL 实现主键自增
    username VARCHAR(50) NOT NULL UNIQUE, -- 用户名不允许为空且必须唯一
    password VARCHAR(100) NOT NULL   -- 密码字段
);