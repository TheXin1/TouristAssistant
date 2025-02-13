CREATE TABLE map_marker (
                            id INT PRIMARY KEY AUTO_INCREMENT,  -- 主键，自增 ID
                            latitude DECIMAL(10, 6) NOT NULL,   -- 纬度
                            longitude DECIMAL(10, 6) NOT NULL,  -- 经度
                            title VARCHAR(255),                 -- 标题
                            icon_path VARCHAR(255),             -- 图标路径
                            width INT DEFAULT 20,               -- 图标宽度
                            height INT DEFAULT 20               -- 图标高度
);
