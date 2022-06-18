package com.games.tap.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.games.tap.domain.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    private static final String JWT_SECRET_KEY = "qat1qat2qat3tap2tap1tap";
    private static final long EXPIRE_TIME = 10 * 60 * 1000;

    /**
     * 注册token
     *
     */
    public static String createToken(UserInfo user) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String token="";
        try{
            token= JWT.create()
                    .withSubject(user.toString())
                    .withClaim("uid", user.getUId()) // 将 user id 保存到 token 里面
                    .withClaim("username", user.getUsername())
                    .withExpiresAt(date) //十分钟后token过期
                    .sign(Algorithm.HMAC256(JWT_SECRET_KEY));
        }catch (Exception e){
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
            } catch (TokenExpiredException e) {
                log.info("token已经过期");
                return false;
            } catch (Exception e) {
                log.warn("token验证失败");
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 检查token是否需要更新
     *
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
     * 检查Token所有Claims
     *
     * @param token Token
     * @return Claims
     */
    public static Map<String, Object> parseToken(String token) {

        //验证JWT
        DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC256(JWT_SECRET_KEY))
                .build().verify(token);
        //获取JWT中的数据,注意数据类型一定要与添加进去的数据类型一致,否则取不到数据
        Map<String, Object> map = new HashMap<>();
        map.put("userId", decodedJwt.getClaim("uid").asLong());
        map.put("username", decodedJwt.getClaim("username").asString());
        map.put("expire", decodedJwt.getExpiresAt());
        return map;
    }
}

