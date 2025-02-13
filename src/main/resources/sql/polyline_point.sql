CREATE TABLE polyline_point (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                polyline_id INT,                      -- 关联 polyline
                                latitude DECIMAL(10, 6) NOT NULL,     -- 纬度
                                longitude DECIMAL(10, 6) NOT NULL,    -- 经度
                                FOREIGN KEY (polyline_id) REFERENCES polyline(id) ON DELETE CASCADE
);
