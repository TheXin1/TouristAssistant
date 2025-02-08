package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.pojo.Results;
import org.datateam.touristassistant.pojo.User;
import org.datateam.touristassistant.service.UserService;
import org.datateam.touristassistant.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/wechat")
public class WeChatLoginController {

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    private static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> login(@RequestParam(value = "avatar") MultipartFile avatar,@RequestParam(value = "nickname") String nickname,@RequestParam("code") String code) {
        // 调用微信 API 获取 openid
        String url = WECHAT_LOGIN_URL + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String response = restTemplate.getForObject(url, String.class);
        // 解析 JSON 响应
        JSONObject jsonResponse = new JSONObject(response);
        if (!jsonResponse.has("openid")) {
            return ResponseEntity.badRequest().body(new Results(500,false));
        }

        String openid = jsonResponse.getString("openid");

        // 查询用户
        User user = userService.findByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname(nickname);
            String path=userService.saveAvatar(avatar,openid);
            user.setAvatar_url(path);
            userService.registerNewUser(user);

        }
        Map<String, Object> claims =Map.of("openid", openid, "nickname", user.getNickname());
        // 生成 JWT token
        String token = JwtUtil.genToken(claims);
        Map<String,Object> results=Map.of("avatar", avatar, "nickname", user.getNickname(),"token",token);
        return ResponseEntity.ok(new Results(200,true,"注册成功",results));

    }



}
