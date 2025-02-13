package org.datateam.touristassistant.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.datateam.touristassistant.pojo.MapPolyline;

import java.util.List;

@Mapper
public interface MapPolylineMapper {
    List<MapPolyline> getAllPolylines();  // 获取所有路线

    void insertPolyline(MapPolyline polyline);  // 插入新路线
}

