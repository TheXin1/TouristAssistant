package org.datateam.touristassistant.pojo;

import lombok.Data;

@Data
public class MapMarker {
    private Integer id;
    private Double latitude;
    private Double longitude;
    private String title;
    private String iconPath;
    private Integer width;
    private Integer height;
}

