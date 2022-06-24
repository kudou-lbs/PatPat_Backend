package com.games.tap.util;

import com.games.tap.domain.ForumUser;
import com.games.tap.domain.User;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ToolUtil {

    public User getUserByToken(String token) {
        return JwtUtil.parseUser(token);
    }

    public static Long getIdByToken(String token) {
        return JwtUtil.parseUserId(token);
    }

    public static Echo checkUser(User user) {
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

    public static boolean checkPassword(String password) {
        //密码长度为6到20位,必须包含字母和数字，字母区分大小写
        String regEx1 = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,20})$";
        Pattern Password_Pattern = Pattern.compile(regEx1);
        Matcher matcher = Password_Pattern.matcher(password);
        return matcher.matches();
    }

    public static void checkExp(ForumUser forumUser){
        int level=forumUser.getLevel();
         int max=maxExp(forumUser.getLevel());
         if(max> forumUser.getExp())return;
         if(level==5){
             forumUser.setExp(max);
             return;
         }
         forumUser.setLevel(level+1);
         forumUser.setExp(maxExp(level+1)/2);
    }

    public static int expRatio(int identity){
        switch (identity){
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            default:
                return 5;
        }
    }

    public static int maxExp(int level){
        switch (level){
            case 1:
                return 40;
            case 2:
                return 100;
            case 3:
                return 300;
            case 4:
                return 700;
            case 5:
                return 1000;
            default:
                return -1;
        }
    }
}
