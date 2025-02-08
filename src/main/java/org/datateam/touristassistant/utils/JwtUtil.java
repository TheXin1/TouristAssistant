package org.datateam.touristassistant.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET_KEY = "TheXin"; // 统一密钥
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 小时

    // 生成 token（支持业务数据）
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    // 解析 token
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

    // 生成 token（仅存储 openid）
    public static String generateToken(String openid) {
        return JWT.create()
                .withSubject(openid)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
}
