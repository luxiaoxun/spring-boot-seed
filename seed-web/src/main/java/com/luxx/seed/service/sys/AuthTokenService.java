package com.luxx.seed.service.sys;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.luxx.seed.model.system.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class AuthTokenService {
    @Value("${sa-token.jwt-secret-key:jwt_secret_key_256}")
    private String jwtSecretKey;

    @Value("${sa-token.timeout:3600}")
    private Long jwtExpireTime;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(jwtSecretKey);
    }

    public String jwtSecretKey() {
        return jwtSecretKey;
    }

    public String createToken(Object loginId, String device, long timeout, Map<String, Object> extraData) {
        return JWT.create()
                .withIssuer("auth0")
                .withAudience(loginId.toString(), device)
                .withExpiresAt(new Date(System.currentTimeMillis() + timeout * 1000))
                .withClaim("extraMap", extraData)
                .sign(algorithm);
    }

    public String getToken(User user) {
        return JWT.create()
                .withIssuer("auth0")
                .withAudience(Long.toString(user.getId()), user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpireTime * 1000))
                .sign(algorithm);
    }

    public boolean verifyToken(String token) {
        // 验证JWT的合法性
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            log.warn(ex.toString());
            return false;
        }
    }

    public Object getExtraValue(String token, String key) {
        // 从JWT中获取Claims
        Claim claim = JWT.decode(token).getClaims().get("extraMap");
        return claim.asMap().getOrDefault(key, null);
    }

}
