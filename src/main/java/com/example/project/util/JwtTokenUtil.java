package com.example.project.util;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.enums.JwtTokenStatus;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * @author 이승환
 * @since 2022-02-19
 */
@Service
public class JwtTokenUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // 토큰 암호화 키

    /**
     * 토큰생성
     *
     * @param name  이름
     * @param regNo 주민등록번호
     * @return token
     */
    public HashMap<String, String> createToken(String name, String regNo) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MILLISECOND, 1800000);    // 만료시간 30분
        Date expiryDate = calendar.getTime();

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, String> payloads = new HashMap<>();
        payloads.put("name", name);
        payloads.put("regNo", regNo);

        String token = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(date)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);

        return map;
    }

    /**
     * 토큰검증
     *
     * @param token 토큰
     * @return 검증결과(true OR false)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 토큰 decoder
     *
     * @param jwtTokenDto   User Token
     * @return payload
     */
    public Object decoderToken(JwtTokenDto jwtTokenDto) {
        String[] chunks = jwtTokenDto.getToken().split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        if (!validateToken(jwtTokenDto.getToken()))
            return JwtTokenStatus.TOKEN_FAILURE;

        String strToken = new String(decoder.decode(chunks[1]));

        return new JSONObject(strToken);
    }
}
