package com.games.tap.interceptor;

import com.games.tap.domain.User;
import com.games.tap.mapper.UserMapper;
import com.games.tap.util.Echo;
import com.games.tap.util.JwtUtil;
import com.games.tap.util.PassToken;
import com.games.tap.util.RetCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//       log.info("=======进入拦截器========");
        // 如果不是映射到方法直接通过,可以访问资源.
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        //检查是否有passToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        log.info("拦截"+request.getRequestURI());
        // 从 http 请求头中取出 token
        String token = request.getHeader("token");
        if (null == token || "".equals(token.trim())) {
            log.warn("token为空");
            JwtUtil.failMessage(response);
            return false;//为空就返回错误
        }
        //1.判断 token 是否过期
        if (JwtUtil.isValid(token)) {
            log.info("token验证成功");
        } else {//token过期就返回 token 无效.
            log.info("token 无效");
            JwtUtil.failMessage(response);
            return false;
        }
        log.info("token:" + token);
        Long userId = JwtUtil.parseUserId(token);
        //2.超过token刷新时间，刷新 token
        if (JwtUtil.isNeedUpdate(token)) {
            User user= userMapper.getUserById(userId);
            if(user!=null){
                response.setHeader("token", JwtUtil.createToken(user));
                log.info("token刷新成功");
            }else
                log.info("token刷新失败");
        }
        log.info("处理完成");
        return true;
    }
}
