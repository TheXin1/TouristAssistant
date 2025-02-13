package org.datateam.touristassistant.pojo;

import lombok.Data;

@Data
public class MapPoint {
    private Integer id;
    private Integer polylineId;
    private Double latitude;
    private Double longitude;
}

