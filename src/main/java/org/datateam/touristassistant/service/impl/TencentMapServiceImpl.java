package org.datateam.touristassistant.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.datateam.touristassistant.mapper.LocationMapper;
import org.datateam.touristassistant.pojo.Location;
import org.datateam.touristassistant.pojo.TencentMapResponse;
import org.datateam.touristassistant.service.TencentMapService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class TencentMapServiceImpl implements TencentMapService {

    @Value("${tencent.map.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final LocationMapper locationMapper;
    private final ObjectMapper objectMapper;

    public TencentMapServiceImpl(RestTemplate restTemplate, LocationMapper locationMapper, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.locationMapper = locationMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public Location getLocationByAddress(String address) {
        // 查询数据库是否已有此地址的经纬度
        Location location = locationMapper.findByAddress(address);
        if (location != null) {
            return location;  // 如果数据库中已有记录，直接返回
        }

        // 否则调用腾讯地图API获取经纬度
        String url = UriComponentsBuilder.fromHttpUrl("https://apis.map.qq.com/ws/geocoder/v1/")
                .queryParam("address", address)
                .queryParam("key", apiKey)
                .toUriString();

        // 调用腾讯地图API并获取返回结果
        String response = restTemplate.getForObject(url, String.class);

        try {
            // 使用 Jackson ObjectMapper 解析 JSON 响应
            TencentMapResponse tencentMapResponse = objectMapper.readValue(response, TencentMapResponse.class);

            // 检查返回状态
            if (tencentMapResponse.getStatus() == 0) {
                // 提取经纬度
                double latitude = tencentMapResponse.getResult().getLocation().getLat();
                double longitude = tencentMapResponse.getResult().getLocation().getLng();

                // 创建新的 Location 对象
                location = new Location();
                location.setAddress(address);
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                // 存储到数据库
                locationMapper.insertLocation(location);

                return location;
            } else {
                // 返回状态不是0时处理错误
                throw new RuntimeException("Error from Tencent Map API: " + tencentMapResponse.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while parsing the Tencent Map API response", e);
        }
    }
}