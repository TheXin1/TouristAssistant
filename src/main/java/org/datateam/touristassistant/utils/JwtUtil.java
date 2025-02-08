package org.datateam.touristassistant.utils;



import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String KEY = "TheXin";
	//接收业务数据,生成token并返回
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
                .sign(Algorithm.HMAC256(KEY));
    }

	//接收token,验证token,并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }
    //验证JWT是否过期
    public boolean isTokenExpiration(String token){
        return getExpirationDateFromToken(token).before(new Date());
    }

    //获取过期时间
    private Date getExpirationDateFromToken(String token){
        return JWT.decode(token).getExpiresAt();
    }

}
