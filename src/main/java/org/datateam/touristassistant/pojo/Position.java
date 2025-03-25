package org.datateam.touristassistant.pojo;

import lombok.Data;

@Data
// 接收前端的用户当前位置的信息

public class Position {
    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
