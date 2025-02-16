package org.datateam.touristassistant.service;

import org.datateam.touristassistant.pojo.Location;

public interface TencentMapService {
    Location getLocationByAddress(String address);
}
