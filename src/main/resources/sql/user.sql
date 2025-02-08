CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,        -- 用户ID，自增主键
                      openid VARCHAR(255) NOT NULL UNIQUE,       -- 微信小程序的唯一标识符
                      nickname VARCHAR(255) NOT NULL,            -- 用户昵称
                      avatar_url VARCHAR(255),                   -- 用户头像URL
                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间，默认当前时间
                      update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- 更新时间，每次更新时会自动更新
);