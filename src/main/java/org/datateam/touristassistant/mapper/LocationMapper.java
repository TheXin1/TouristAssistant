package org.datateam.touristassistant.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.datateam.touristassistant.pojo.Location;

public interface LocationMapper {

    void insertLocation(Location location);

    Location findByAddress(String address);
}