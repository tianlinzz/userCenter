package com.tianlin.usercenter.utils;

import com.tianlin.usercenter.model.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.alibaba.fastjson.JSON;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtUtil {

    public static final SecretKey JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 过期时间，单位为秒，这里设置为 1 小时
    public static final long EXPIRE_TIME = 3600 * 72; // 3 天

    // 生成 JWT 签名
    private static String generateToken(Map<String, Object> claims) {
        // 生成签发时间和过期时间
        Date now = new Date();
        Date expireTimeDate = new Date(now.getTime() + EXPIRE_TIME);

        // 生成 JWT
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireTimeDate)
                .signWith(JWT_SECRET)
                .compact();
    }

    // 解析 JWT 签名
    public static Long parseToken(String token) {
        // 解析 JWT
        Claims parsedClaims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(getToken(token))
                .getBody();
        User user = JSON.parseObject(JSON.toJSONString(parsedClaims.get("userInfo")), User.class);
        return user.getId();
    }

    // 检查token是否过期
    public static boolean isTokenExpired(String token) {
        // 解析 JWT
        Claims parsedClaims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(getToken(token))
                .getBody();
        Date expiration = parsedClaims.getExpiration();
        return expiration.before(new Date());
    }

    // 让当前token过期
    public static void invalidateToken(String token) {
        // 解析 JWT
        Claims parsedClaims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(getToken(token))
                .getBody();
        // 让当前token过期
        parsedClaims.setExpiration(new Date());
    }

    public static String getToke(User user) {
        // 生成 JWT 签名
        Map<String, Object> claims = new HashMap<>();
        user.setUserPassword(null);
        claims.put("userInfo", user);
        return JwtUtil.generateToken(claims);
    }
    // 出去token中"Bearer "的前缀, 以便后续解析
    private static String getToken(String token) {
        return token.substring(7);
    }
}