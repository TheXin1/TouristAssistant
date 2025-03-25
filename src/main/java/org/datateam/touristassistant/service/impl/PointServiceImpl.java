package org.datateam.touristassistant.service.impl;

import org.datateam.touristassistant.mapper.PointMapper;
import org.datateam.touristassistant.pojo.Point;
import org.datateam.touristassistant.pojo.Position;
import org.datateam.touristassistant.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {

    @Autowired // 使用 @Autowired 注入依赖
    private PointMapper pointMapper;

    @Override
    public Point getPointById(Integer id) {
        return pointMapper.getPointById(id);
    }

    @Override
    public List<Integer> getNearbyPointIds(Position position) {
        double lat = position.getLatitude();
        double lon = position.getLongitude();
        double radius = 0.05; // 约 5km

        return pointMapper.getNearbyPointIds(lat - radius, lat + radius, lon - radius, lon + radius);
    }

    @Override
    public List<Point> getAllPoints() {
        return pointMapper.getAllPoints();
    }
}