package org.datateam.touristassistant.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.datateam.touristassistant.pojo.User;

@Mapper
public interface UserMapper {

    // 根据 openid 查找用户
    User findByOpenid(String openid);

    // 插入新用户
    int insertUser(User user);

    // 更新用户头像
    int updateAvatar(String openid, String avatar_url);
}
