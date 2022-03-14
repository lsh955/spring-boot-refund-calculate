package com.example.project.app.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * @author 이승환
 * @since 2022-02-19
 */
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

        String token = Jwts.builder()   // jjwt library의 Jwts로부터 jwt을 생성
                .setHeader(headers)     // JWT Header가 지닐 정보
                .setClaims(payloads)    // JWT Payload가 지닐 정보
                .setIssuedAt(date)      // 발급 시각
                .setExpiration(expiryDate)  // 만료시간
                .signWith(secretKey, SignatureAlgorithm.HS256)  // 해싱할 알고리즘과 비밀키
                .compact();

        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);

        return map;
    }

    /**
     * 토큰 decoder
     *
     * @param token     User Token
     * @return          payload
     */
    public HashMap<String, String> decoderToken(String token) throws Exception {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();    // base64 인코딩 체계를 사용하여 디코딩

        String strToken = new String(decoder.decode(chunks[1]));    // split 으로 짤라진 것중에서 특정배열값을 가져와 디코드
        JSONObject jsonObject = new JSONObject(strToken);   // String을 json 객체로 변환

        HashMap<String, String> refunds = new HashMap<>();
        refunds.put("name", jsonObject.get("name").toString());
        refunds.put("regNo", jsonObject.get("regNo").toString());
        refunds.put("exp", jsonObject.get("exp").toString());
        refunds.put("iat", jsonObject.get("iat").toString());

        return refunds;
    }
}
