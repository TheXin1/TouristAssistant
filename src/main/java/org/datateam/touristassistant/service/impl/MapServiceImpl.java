package org.datateam.touristassistant.service.impl;

import org.datateam.touristassistant.mapper.MapMarkerMapper;
import org.datateam.touristassistant.mapper.MapPolylineMapper;
import org.datateam.touristassistant.pojo.MapMarker;
import org.datateam.touristassistant.pojo.MapPolyline;
import org.datateam.touristassistant.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapServiceImpl implements MapService {

    @Autowired
    private MapMarkerMapper markerMapper;

    @Autowired
    private MapPolylineMapper polylineMapper;

    @Override
    public List<MapMarker> getMarkers() {
        return markerMapper.getAllMarkers();
    }

    @Override
    public void addMarker(MapMarker marker) {
        markerMapper.insertMarker(marker);
    }

    @Override
    public void deleteMarker(Integer id) {
        markerMapper.deleteMarker(id);
    }

    @Override
    public List<MapPolyline> getPolylines() {
        return polylineMapper.getAllPolylines();
    }

    @Override
    public void addPolyline(MapPolyline polyline) {
        polylineMapper.insertPolyline(polyline);
    }
}

