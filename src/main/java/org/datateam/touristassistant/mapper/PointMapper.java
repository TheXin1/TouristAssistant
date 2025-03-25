package org.datateam.touristassistant.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.datateam.touristassistant.pojo.Point;

import java.util.List;

@Mapper
public interface PointMapper {
    Point getPointById(@Param("id") Integer id);

    List<Integer> getNearbyPointIds(@Param("minLat") Double minLat,
                                    @Param("maxLat") Double maxLat,
                                    @Param("minLon") Double minLon,
                                    @Param("maxLon") Double maxLon);

    List<Point> getAllPoints(); // 添加新方法
}
