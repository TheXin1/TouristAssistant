package org.datateam.touristassistant.pojo;

import java.util.List;

// IsPolyline类，用于处理polyline数据
public class IsPolyline<T extends Boolean> {
    private Integer id; // only exists if T is true
    private T isPolyline;
    private List<MapPolyline> polyline; // only exists if T is true

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public T getIsPolyline() {
        return isPolyline;
    }

    public void setIsPolyline(T isPolyline) {
        this.isPolyline = isPolyline;
    }

    public List<MapPolyline> getPolyline() {
        return polyline;
    }

    public void setPolyline(List<MapPolyline> polyline) {
        this.polyline = polyline;
    }
}
