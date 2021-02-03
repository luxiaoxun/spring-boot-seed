package com.luxx.seed.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luxx.seed.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TokenUtil {
    private static final long TokenExpireTime = 24 * 60 * 60 * 1000;
    private static Algorithm algorithm = Algorithm.HMAC256("secret_key_256");

    public static String getToken(User user) {
        String token = JWT.create()
                .withIssuer("auth0")
                .withAudience(Long.toString(user.getId()), user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TokenExpireTime))
                .sign(algorithm);
        return token;
    }

    public static String verifyToken(String token) {
        String userId = "";
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            List<String> audience = jwt.getAudience();
            if (audience != null && audience.size() > 0) {
                userId = audience.get(0);
            }
        } catch (JWTVerificationException exception) {
            log.error("Verify token {} error", token);
        }
        return userId;
    }


    public static String getToken(Map<String, Object> claimMap) {
        String token = JWT.create()
                .withIssuer("auth0")
                .withClaim("info", claimMap)
                .sign(algorithm);
        return token;
    }

    public static Map<String, Object> checkToken(String token) {
        Map<String, Object> ret = new HashMap<>();
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            ret = jwt.getClaim("info").asMap();
        } catch (JWTVerificationException ex) {
            log.error("Check token error: {}, token: {}", ex.toString(), token);
        }
        return ret;
    }

}
