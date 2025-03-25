package org.datateam.touristassistant.pojo;

import lombok.Data;

@Data
// 该类是地点信息类，景区的信息介绍等
public class Point {
    private Integer id;
    private String title;
    private Double latitude;
    private Double longitude;
    private String iconPath;
    private String content;
    private String position;
    private String cover;
    private String tel;
    private String time;
    private String price;
}
