package org.datateam.touristassistant.service;

import org.datateam.touristassistant.pojo.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User findByOpenid(String openid);
    void registerNewUser(User user);
    void updateAvatar(String openid, String avatar_url);
    String saveAvatar(MultipartFile avatar, String name);
}
