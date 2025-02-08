package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.pojo.ResponseMessage;
import org.datateam.touristassistant.pojo.User;
import org.datateam.touristassistant.service.UserService;
import org.datateam.touristassistant.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.Map;

@RestController
@RequestMapping("")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("code") String code, @RequestParam(value = "avatar_url", required = false) String avatarUrl) {
        // 调用微信 API 获取 openid
        String url = WECHAT_LOGIN_URL + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String response = restTemplate.getForObject(url, String.class);
        // 解析 JSON 响应
        JSONObject jsonResponse = new JSONObject(response);
        if (!jsonResponse.has("openid")) {
            return ResponseEntity.badRequest().body(new ResponseMessage("error", "登录失败，微信返回错误", null));
        }

        String openid = jsonResponse.getString("openid");

        // 查询用户
        User user = userService.findByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setAvatar_url(avatarUrl != null ? avatarUrl : ""); // 头像可选
            user.setNickname("默认昵称");
            userService.registerNewUser(user);
        } else if (avatarUrl != null && !avatarUrl.equals(user.getAvatar_url())) {
            // 更新头像（如果提供了新头像）
            userService.updateAvatar(openid, avatarUrl);
            user.setAvatar_url(avatarUrl);
        }
        Map<String, Object> claims =Map.of("openid", openid, "avatar_url", user.getAvatar_url(), "nickname", user.getNickname());
        // 生成 JWT token
        String token = JwtUtil.genToken(claims);

        return ResponseEntity.ok().body(new ResponseMessage("ok", "登录成功", token));
    }
}
