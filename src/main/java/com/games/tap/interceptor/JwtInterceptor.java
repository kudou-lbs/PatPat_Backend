package com.games.tap.interceptor;

import com.games.tap.domain.UserInfo;
import com.games.tap.service.UserService;
import com.games.tap.util.JwtUtil;
import com.games.tap.util.PassToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService mUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        log.info("=======进入拦截器========");
        // 如果不是映射到方法直接通过,可以访问资源.
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        log.info(request.getRequestURI());
        Method method = ((HandlerMethod) handler).getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        // 从 http 请求头中取出 token
        String token = request.getHeader("token");
        if (null == token || "".equals(token.trim())) {
            log.warn("token为空");
            return false;//为空就返回错误
        }
        //1.判断 token 是否过期
        if (JwtUtil.isValid(token)) {
            log.info("token验证成功");
        } else {//token过期就返回 token 无效.
            throw new RuntimeException("token 无效");
//            return false; FIXME
        }
        log.info("==============token:" + token);
        Map<String, Object> map = JwtUtil.parseToken(token);
        Long userId = Long.parseLong(String.valueOf(map.get("userId")));
        //2.超过token刷新时间，刷新 token
        if (JwtUtil.isNeedUpdate(token)) {
            UserInfo user= mUserService.getUserById(userId);//可能有问题
            response.setHeader("token", JwtUtil.createToken(user));
            log.info("token刷新成功");
        }
        return true;
    }
}
