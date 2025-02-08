package org.datateam.touristassistant.service.impl;

import org.datateam.touristassistant.mapper.UserMapper;
import org.datateam.touristassistant.pojo.User;
import org.datateam.touristassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 根据 openid 查找用户
    @Override
    public User findByOpenid(String openid) {
        return userMapper.findByOpenid(openid);
    }

    // 注册新用户
    @Override
    public void registerNewUser(User user) {
        userMapper.insertUser(user);
    }

    // 更新用户头像
    @Override
    public void updateAvatar(String openid, String avatar_url) {
        userMapper.updateAvatar(openid, avatar_url);
    }
}
