package com.games.tap.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.tap.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    private static final String JWT_SECRET_KEY = "qat1qat2qat3tap2tap1tap";
    private static final long EXPIRE_TIME = 8 * 60 * 60 * 1000;
    private static final ObjectMapper om = new ObjectMapper();

    /**
     * 注册token
     */
    public static String createToken(User user) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String token = "";
        try {
            token = JWT.create()
                    .withSubject(om.writeValueAsString(user))
                    .withClaim("uid", user.getUId()) // 将 user id 保存到 token 里面
                    .withClaim("username", user.getUsername())
                    .withExpiresAt(date) //十分钟后token过期
                    .sign(Algorithm.HMAC256(JWT_SECRET_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 检查Token是否有效
     *
     * @param token Token
     * @return 是否有效
     */
    public static boolean isValid(String token) {
        //获取token过期时间
        Date expiresAt;
        if (Strings.isNotBlank(token)) {
            try {
                expiresAt = JWT.require(Algorithm.HMAC256(JWT_SECRET_KEY))
                        .build()
                        .verify(token)
                        .getExpiresAt();
                return new Date().before(expiresAt);
            }catch (JWTDecodeException e){
                log.info("token验证失败");
                return false;
            } catch (TokenExpiredException e) {
                log.info("token已经过期");
                return false;
            } catch (Exception e) {
                log.warn("token出现未知错误");
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 检查token是否需要更新
     */
    public static boolean isNeedUpdate(String token) {
        //获取token过期时间
        Date expiresAt = null;
        try {
            expiresAt = JWT.require(Algorithm.HMAC256(JWT_SECRET_KEY))
                    .build()
                    .verify(token)
                    .getExpiresAt();
        } catch (TokenExpiredException e) {
            return true;
        } catch (Exception e) {
            log.warn("token验证失败");
        }
        //如果剩余过期时间少于过期时常的一半时 需要更新
        assert expiresAt != null;
        return (expiresAt.getTime() - System.currentTimeMillis()) < (EXPIRE_TIME >> 1);
    }

    /**
     * 验证JWT,必须先验证合法性
     *
     * @param token Token
     * @return DecodedJWT
     */
    public static DecodedJWT parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(JWT_SECRET_KEY))
                .build().verify(token);
    }

    public static User parseUser(String token) {
        try {
            return om.readValue(parseToken(token).getSubject(), User.class);//可能会有问题
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long parseUserId(String token) {
        return parseToken(token).getClaim("uid").asLong();
    }

    public static Map<String, Object> parseAllInfo(String token) {
        DecodedJWT decodedJWT = parseToken(token);
        //获取JWT中的数据,注意数据类型一定要与添加进去的数据类型一致,否则取不到数据
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("user", om.readValue(decodedJWT.getSubject(), User.class));
            map.put("userId", decodedJWT.getClaim("uid").asLong());
            map.put("username", decodedJWT.getClaim("username").asString());
            map.put("expire", decodedJWT.getExpiresAt());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void failMessage(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("content-type", "application/json;charset=utf-8");
        try {
            response.getWriter().write(om.writeValueAsString(Echo.define(RetCode.USER_NOT_LOGGED_IN)));
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("写响应失败");
        }
    }
}

