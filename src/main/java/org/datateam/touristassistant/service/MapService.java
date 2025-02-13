package org.datateam.touristassistant.service;

import org.datateam.touristassistant.pojo.MapMarker;
import org.datateam.touristassistant.pojo.MapPolyline;

import java.util.List;

public interface MapService {
    List<MapMarker> getMarkers();  // 获取所有标记点
    void addMarker(MapMarker marker);  // 添加标记点
    void deleteMarker(Integer id);  // 删除标记点

    List<MapPolyline> getPolylines();  // 获取所有路线
    void addPolyline(MapPolyline polyline);  // 添加路线
}

