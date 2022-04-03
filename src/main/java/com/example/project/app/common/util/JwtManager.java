package com.example.project.app.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * @author 이승환
 * @since 2022-04-03
 */
@AllArgsConstructor
public class JwtManager {

    private String secretKey;
    private long TOKEN_VALIDATiON_SECOND = 60;

    // secretKey 로드
    private Key getSigninKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰생성
    public String generateToken(String username, String regNo) {
        Claims claims = Jwts.claims();
        claims.put("name", username);
        claims.put("regNo", regNo);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (TOKEN_VALIDATiON_SECOND * 1000)))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰정보 return
    public TokenInfo getTokenInfo(String token) {
        Claims body = getClaims(token);

        String username = body.get("name", String.class);
        String regNo = body.get("regNo", String.class);
        Date issuedAt = body.getIssuedAt();
        Date expiration = body.getExpiration();

        return new TokenInfo(username, regNo, issuedAt, expiration);
    }

    // 토큰정보 해석
    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TokenInfo {
        private String name;
        private String regNo;
        private Date issuedAt;
        private Date expire;
    }
}
