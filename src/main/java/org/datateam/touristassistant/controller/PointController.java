package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.pojo.Point;
import org.datateam.touristassistant.pojo.Position;
import org.datateam.touristassistant.pojo.ResponseData;
import org.datateam.touristassistant.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
public class PointController {

    @Autowired // 使用 @Autowired 注入依赖
    private PointService pointService;

    @GetMapping("/{id}")
    public ResponseData<Point> getPointById(@PathVariable Integer id) {
        Point point = pointService.getPointById(id);
        if (point != null) {
            return new ResponseData<>("200", true, "查询成功", point);
        } else {
            return new ResponseData<>("404", false, "地点未找到", null);
        }
    }

    @PostMapping("/nearby-ids")
    public ResponseData<List<Integer>> getNearbyPointIds(@RequestBody Position position) {
        List<Integer> pointIds = pointService.getNearbyPointIds(position);
        return new ResponseData<>("200", true, "查询成功", pointIds);
    }

    @GetMapping("/all")
    public ResponseData<List<Point>> getAllPoints() {
        List<Point> points = pointService.getAllPoints();
        return new ResponseData<>("200", true, "查询成功", points);
    }
}