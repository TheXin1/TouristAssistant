package org.datateam.touristassistant.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.datateam.touristassistant.pojo.MapMarker;

import java.util.List;

@Mapper
public interface MapMarkerMapper {
    List<MapMarker> getAllMarkers();  // 获取所有标记点

    void insertMarker(MapMarker marker);  // 插入新标记点

    void deleteMarker(Integer id);  // 删除标记点
}

