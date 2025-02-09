package org.datateam.touristassistant.service.impl;

import org.datateam.touristassistant.mapper.UserMapper;
import org.datateam.touristassistant.pojo.User;
import org.datateam.touristassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Value("${wechat.avatarPath}")
    private String avatarPath;

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
            // 获取文件的原始扩展名
            String originalFilename = avatar.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 设置文件的保存路径
            File saveFile = new File(avatarPath + name + fileExtension);

            // 确保目录存在
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }

            // 将文件保存到指定目录
            avatar.transferTo(saveFile);

            // 返回相对路径(浏览器访问数据)
            return avatarPath + name + fileExtension;
        } catch (IOException e) {
            e.printStackTrace();
            // 返回失败信息或其他默认值
            return "error";
        }
    }

}
