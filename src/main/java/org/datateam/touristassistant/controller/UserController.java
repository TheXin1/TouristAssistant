package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.pojo.User;
import org.datateam.touristassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String createUser(@RequestBody User user) {
        userService.addUser(user);
        return "User created successfully!";
    }

    // 根据ID获取用户
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    // 获取所有用户
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 更新用户
    @PutMapping("/{userId}")
    public String updateUser(@PathVariable Long userId, @RequestBody User user) {
        user.setUserId(userId);
        userService.updateUser(user);
        return "User updated successfully!";
    }

    // 删除用户
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "User deleted successfully!";
    }
}
