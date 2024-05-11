package com.luxx.seed.util;

import cn.dev33.satoken.stp.StpUtil;

public class UserUtil {

    public static String getLoginUser() {
        return StpUtil.getLoginIdAsString();
    }

}
