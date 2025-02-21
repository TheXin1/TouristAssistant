package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.pojo.MapMarker;
import org.datateam.touristassistant.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wechat/map")
public class MapController {
    @Autowired
    private MapService mapService;

    @GetMapping("/markers")
    public ResponseEntity<List<MapMarker>> getMarkers() {
        return ResponseEntity.ok(mapService.getMarkers());
    }

    @PostMapping("/markers")
    public ResponseEntity<String> addMarker(@RequestBody MapMarker marker) {
        mapService.addMarker(marker);
        return ResponseEntity.ok("标记点添加成功");
    }

    @DeleteMapping("/markers/{id}")
    public ResponseEntity<String> deleteMarker(@PathVariable Integer id) {
        mapService.deleteMarker(id);
        return ResponseEntity.ok("标记点删除成功");
    }
}

