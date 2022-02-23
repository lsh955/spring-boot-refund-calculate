package com.example.project.util;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.enums.JwtTokenStatus;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * @author 이승환
 * @since 2022-02-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final AESCryptoUtil aesCryptoUtil;

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // 토큰 암호화 키

    /**
     * 토큰생성
     *
     * @param name  이름
     * @param regNo 주민등록번호
     * @return token
     */
    public HashMap<String, String> createToken(String name, String regNo) throws Exception {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MILLISECOND, 1800000);    // 만료시간 30분
        Date expiryDate = calendar.getTime();

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, String> payloads = new HashMap<>();
        payloads.put("name", name);
        payloads.put("regNo", this.aesCryptoUtil.decrypt(regNo));

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
    public JwtTokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return JwtTokenStatus.TOKEN_SUCCESS;
        } catch (JwtException e) {
            e.printStackTrace();
            return JwtTokenStatus.TOKEN_FAILURE;
        }
    }

    /**
     * 토큰 decoder
     *
     * @param jwtTokenDto User Token
     * @return payload
     */
    public HashMap<String, String> decoderToken(JwtTokenDto jwtTokenDto) throws Exception {
        String[] chunks = jwtTokenDto.getToken().split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String strToken = new String(decoder.decode(chunks[1]));
        JSONObject jsonObject = new JSONObject(strToken);

        HashMap<String, String> refunds = new HashMap<>();
        refunds.put("name", jsonObject.get("name").toString());
        refunds.put("regNo", jsonObject.get("regNo").toString());
        refunds.put("exp", jsonObject.get("exp").toString());
        refunds.put("iat", jsonObject.get("iat").toString());

        return refunds;
    }
}
