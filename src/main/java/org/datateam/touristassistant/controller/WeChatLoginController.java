package org.datateam.touristassistant.controller;

import org.datateam.touristassistant.pojo.Results;
import org.datateam.touristassistant.pojo.User;
import org.datateam.touristassistant.service.UserService;
import org.datateam.touristassistant.utils.JwtUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/wechat")
public class WeChatLoginController {

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.avatar.dir}")
    private String avatarDir;

    @Autowired
    private RestTemplate restTemplate;

    private static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserService userService;

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("code") String code) {
        // 调用微信 API 获取 openid
        String url = WECHAT_LOGIN_URL + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String response = restTemplate.getForObject(url, String.class);

        // 解析 JSON 响应
        JSONObject jsonResponse = new JSONObject(response);
        if (!jsonResponse.has("openid")) {
            return ResponseEntity.badRequest().body(new Results(500, false, "登录失败"));
        }

        String openid = jsonResponse.getString("openid");

        // 查询用户
        User user = userService.findByOpenid(openid);
        if (user == null) {
            // 如果是第一次登录
            return ResponseEntity.ok(new Results(200, true, "登录成功", Map.of("isFirst", true)));
        } else {
            // 如果已经注册，返回用户信息
            // 生成 JWT token
            Map<String, Object> claims = Map.of(
                    "openid", openid,
                    "avatar_url", user.getAvatar_url(),
                    "nickname", user.getNickname()
            );
            String token = JwtUtil.genToken(claims);

            Map<String, Object> results = Map.of(
                    "isFirst", false,
                    "avatar", user.getAvatar_url(),
                    "nickname", user.getNickname(),
                    "token", token
            );
            return ResponseEntity.ok(new Results(200, true, "登录成功", results));
        }
    }

    // 注册接口
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam("code") String code) {

        // 调用微信 API 获取 openid
        String url = WECHAT_LOGIN_URL + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String response = restTemplate.getForObject(url, String.class);

        // 解析 JSON 响应
        JSONObject jsonResponse = new JSONObject(response);
        if (!jsonResponse.has("openid")) {
            return ResponseEntity.badRequest().body(new Results(500, false, "注册失败"));
        }

        String openid = jsonResponse.getString("openid");

        // 查询用户
        User user = userService.findByOpenid(openid);
        if (user != null) {
            return ResponseEntity.status(400).body(new Results(500, false, "用户已存在"));
        }

        // 创建新用户
        user = new User();
        user.setOpenid(openid);
        user.setNickname(nickname);

        // 如果上传了头像，则保存头像并设置路径
        if (avatar != null) {
            String avatarPath = userService.saveAvatar(avatar, openid);
            user.setAvatar_url(avatarPath);
        }

        userService.registerNewUser(user);

        // 生成 JWT token
        Map<String, Object> claims = Map.of(
                "openid", openid,
                "avatar_url", user.getAvatar_url(),
                "nickname", user.getNickname()
        );
        String token = JwtUtil.genToken(claims);

        Map<String, Object> results = Map.of(
                "avatar", user.getAvatar_url(),
                "nickname", user.getNickname(),
                "token", token
        );

        return ResponseEntity.ok(new Results(200, true, "注册成功", results));
    }
}
