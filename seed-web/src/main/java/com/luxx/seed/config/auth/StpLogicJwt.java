package com.luxx.seed.config.auth;

import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.luxx.seed.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("stpLogic")
public class StpLogicJwt extends StpLogic {

    @Autowired
    private AuthTokenService authTokenService;

    /**
     * Sa-Token 整合 jwt -- Simple模式
     */
    public StpLogicJwt() {
        super(StpUtil.TYPE);
    }

    /**
     * 获取jwt秘钥
     *
     * @return /
     */
    public String jwtSecretKey() {
        return authTokenService.jwtSecretKey();
    }


    // ------ 重写方法

    /**
     * 创建一个TokenValue
     */
    @Override
    public String createTokenValue(Object loginId, String device, long timeout, Map<String, Object> extraData) {
        return authTokenService.createToken(loginId, device, timeout, extraData);
    }

    /**
     * 获取当前 Token 的扩展信息
     */
    @Override
    public Object getExtra(String key) {
        return getExtra(getTokenValue(), key);
    }

    /**
     * 获取指定 Token 的扩展信息
     */
    @Override
    public Object getExtra(String tokenValue, String key) {
        return authTokenService.getExtraValue(tokenValue, key);
    }


    @Override
    public boolean getConfigOfIsShare() {
        // 为确保 jwt-simple 模式的 token Extra 数据生成不受旧token影响，这里必须让 is-share 恒为 false
        // 即：在使用 jwt-simple 模式后，即使配置了 is-share=true 也不能复用旧 Token，必须每次创建新 Token
        return false;
    }

    /**
     * 重写返回：支持 extra 扩展参数
     */
    @Override
    public boolean isSupportExtra() {
        return true;
    }
}
