package com.games.tap.service;

import com.games.tap.domain.User;
import com.games.tap.mapper.UserMapper;
import com.games.tap.util.Echo;
import com.games.tap.util.JwtUtil;
import com.games.tap.util.RetCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    public User getUserByToken(String token) {
        return JwtUtil.parseUser(token);
    }

    public Long getIdByToken(String token) {
        return JwtUtil.parseUserId(token);
    }

    public Echo checkUser(User user) {
        if (user.getUsername() == null || user.getUsername().equals(""))
            return Echo.fail("账户名不能为空");
        if (user.getPassword() == null || user.getPassword().equals(""))
            return Echo.fail("密码不能为空");
        if (!checkPassword(user.getPassword()))
            return Echo.fail("密码格式错误");
        return null;
    }

    public static Echo checkList(String id, String offset, String pageSize) {
        if (id != null && !StringUtils.isNumeric(id)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
        if (pageSize != null) {
            if (!StringUtils.isNumeric(pageSize)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            if (Long.parseLong(pageSize) <= 0) return Echo.define(RetCode.PARAM_IS_INVALID);
        }else if(offset!=null)
            return Echo.define(RetCode.PARAM_IS_EMPTY);
        if (offset != null) {
            if (!StringUtils.isNumeric(offset)) return Echo.define(RetCode.PARAM_TYPE_BIND_ERROR);
            if (Long.parseLong(offset) < 0) return Echo.define(RetCode.PARAM_IS_INVALID);
        }
        return null;
    }

    public boolean checkPassword(String password) {
        //密码长度为6到20位,必须包含字母和数字，字母区分大小写
        String regEx1 = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,20})$";
        Pattern Password_Pattern = Pattern.compile(regEx1);
        Matcher matcher = Password_Pattern.matcher(password);
        return matcher.matches();
    }
}
