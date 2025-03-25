package org.datateam.touristassistant.service;

import org.datateam.touristassistant.pojo.Point;
import org.datateam.touristassistant.pojo.Position;

import java.util.List;

public interface PointService {
    Point getPointById(Integer id);
    List<Integer> getNearbyPointIds(Position position);

    List<Point> getAllPoints(); // 添加新方法声明
}