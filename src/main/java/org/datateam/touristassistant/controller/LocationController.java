package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.pojo.Location;
import org.datateam.touristassistant.service.TencentMapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

    private final TencentMapService tencentMapService;

    public LocationController(TencentMapService tencentMapService) {
        this.tencentMapService = tencentMapService;
    }

    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String address) {
        Location location = tencentMapService.getLocationByAddress(address);
        return "Location: " + location.getAddress() + " (" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }
}