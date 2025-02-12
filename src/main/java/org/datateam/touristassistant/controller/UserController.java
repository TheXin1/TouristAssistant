package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.pojo.User;
import org.datateam.touristassistant.service.UserService;
import org.datateam.touristassistant.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;




}
