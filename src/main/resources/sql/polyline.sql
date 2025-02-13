CREATE TABLE polyline (
                          id INT PRIMARY KEY AUTO_INCREMENT,  -- 主键，自增 ID
                          color VARCHAR(10) DEFAULT '#FA6400', -- 颜色
                          width INT DEFAULT 10,               -- 线宽
                          arrow_line BOOLEAN DEFAULT TRUE,    -- 是否有箭头
                          border_width INT DEFAULT 2          -- 边框宽度
);
