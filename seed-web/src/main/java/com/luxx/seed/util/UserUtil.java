package com.luxx.seed.util;

import cn.dev33.satoken.stp.StpUtil;

public class UserUtil {

    public static String getLoginUsername() {
        return StpUtil.getLoginIdAsString();
    }

}
