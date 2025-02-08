package org.datateam.touristassistant.service;

import org.datateam.touristassistant.pojo.User;

public interface UserService {
    User findByOpenid(String openid);
    void registerNewUser(User user);
    void updateAvatar(String openid, String avatar_url);
}
