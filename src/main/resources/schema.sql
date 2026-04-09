-- 在 PostgreSQL 中创建 sys_user 表
CREATE TABLE IF NOT EXISTS sys_user (
    id SERIAL PRIMARY KEY,          -- 使用 SERIAL 实现主键自增
    username VARCHAR(50) NOT NULL UNIQUE, -- 用户名不允许为空且必须唯一
    password VARCHAR(100) NOT NULL   -- 密码字段
);

-- 1. 创建 user_info 用户个人信息表
CREATE TABLE IF NOT EXISTS user_info (
    id SERIAL PRIMARY KEY,
    real_name VARCHAR(50),   -- 真实姓名
    phone VARCHAR(20),       -- 手机号码
    address VARCHAR(200),    -- 联系地址
    user_id INT NOT NULL UNIQUE -- 关联 sys_user 表的 id，UNIQUE 保证一个账号只留一条
);

-- 2. 插入几条测试数据（sys_user 表中插入 id 为 1 和 2 的用户）
-- 注意：如果 sys_user 已经有数据了，插入可能会报错，所以使用 ON CONFLICT 或者简单的 INSERT
INSERT INTO sys_user (id, username, password) VALUES (1, 'user1', '123456') ON CONFLICT (id) DO NOTHING;
INSERT INTO sys_user (id, username, password) VALUES (2, 'user2', '123456') ON CONFLICT (id) DO NOTHING;

INSERT INTO user_info (real_name, phone, address, user_id) 
VALUES ('张三', '13811111111', '北京', 1) 
ON CONFLICT (user_id) DO NOTHING;
INSERT INTO user_info (real_name, phone, address, user_id) 
VALUES ('李四', '13911111111', '上海', 2) 
ON CONFLICT (user_id) DO NOTHING;