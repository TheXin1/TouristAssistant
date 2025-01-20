package org.datateam.touristassistant.service;

import org.datateam.touristassistant.pojo.User;

import java.util.List;

public interface UserService {

    // 添加用户
    void addUser(User user);

    // 根据用户ID查询用户
    User getUserById(Long userId);

    // 查询所有用户
    List<User> getAllUsers();

    // 更新用户信息
    void updateUser(User user);

    // 删除用户
    void deleteUser(Long userId);
}
