package com.games.tap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {

    public static boolean checkPassword(String password) {
        //密码长度为6到20位,必须包含字母和数字，字母区分大小写
        String regEx1 = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,20})$";
        Pattern Password_Pattern = Pattern.compile(regEx1);
        Matcher matcher = Password_Pattern.matcher(password);
        return matcher.matches();
    }
}
