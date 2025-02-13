package org.datateam.touristassistant.pojo;

import lombok.Data;

import java.util.List;

@Data
public class MapPolyline {
    private Integer id;
    private String color;
    private Integer width;
    private Boolean arrowLine;
    private Integer borderWidth;
    private List<MapPoint> points;
}

