CREATE TABLE `poi_location` (
                                `id` INT NOT NULL AUTO_INCREMENT,
                                `name` VARCHAR(255) NOT NULL,
                                `latitude` DOUBLE NOT NULL,
                                `longitude` DOUBLE NOT NULL,
                                `address` VARCHAR(255),
                                `poi_id` VARCHAR(255) UNIQUE,
                                PRIMARY KEY (`id`)
);
