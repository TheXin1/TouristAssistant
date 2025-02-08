package org.datateam.touristassistant.service.impl;

import org.datateam.touristassistant.mapper.UserMapper;
import org.datateam.touristassistant.pojo.User;
import org.datateam.touristassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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

    @Override
    public String saveAvatar(MultipartFile avatar, String name) {
        try {
            // 定义保存路径，这里以项目的根目录为例
            String uploadDir = "avatars";

            // 设置文件的保存路径
            File saveFile = new File(uploadDir + name);

            // 确保目录存在
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }

            // 将文件保存到指定目录
            avatar.transferTo(saveFile);

            // 返回保存的文件路径（可以是相对路径，也可以是绝对路径）
            return uploadDir + name;
        } catch (IOException e) {
            e.printStackTrace();
            // 如果保存失败，可以返回空字符串或者其他默认值
            return "";
        }
    }
}
