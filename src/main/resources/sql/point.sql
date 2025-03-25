CREATE TABLE point (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       latitude DOUBLE NOT NULL,
                       longitude DOUBLE NOT NULL,
                       icon_path VARCHAR(255),
                       content TEXT NOT NULL,
                       position VARCHAR(255) NOT NULL,
                       cover VARCHAR(255),
                       tel VARCHAR(20),
                       time VARCHAR(50),
                       price VARCHAR(50)
);
