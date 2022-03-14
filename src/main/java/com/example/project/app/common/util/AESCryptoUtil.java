package com.example.project.app.common.util;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author 이승환
 * @since 2022-02-23
 */
@Service
public class AESCryptoUtil {

    public static String alg = "AES/CBC/PKCS5Padding";  // 알고리즘,운용모드,패딩방식
    private final String secretKey = "01234567890123456789012345678901";  // 비밀키
    private final String iv = secretKey.substring(0, 16); // 16byte

    /**
     * 암호화 처리
     *
     * @param text 암호화 하려는 평문 데이터
     * @return 암호화된 데이터
     * @throws Exception
     */
    public String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);    // getInstance()에 Cipher 객체를 AES 암호화, CBC operation mode, PKCS5 padding scheme로 초기화
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");   // key 로 비밀키 생성

        /**
         * AES는 128비트(16바이트)단위로 암호화 하기때문에 IV 또한 16바이트 똑같은 크기여야 한다.
         * IV가 생성되면 이 값을 가지고 첫번째 블록을 암호화 하고, 매번 다른 IV를 생성하면 같은 평문이라도 다른 암호문을 생성할 수 있다.
         */
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());   // CBC 암호화는 IV 라는 벡터값이 하나가 더 들어간다고 한다.
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec); // cipher 객체를 암호화 모드로 초기화

        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));   // 평문을 바이트와 하고, 바이트한 평문을 암호화 한다.

        return Base64.getEncoder().encodeToString(encrypted);   // 암호화 인코딩 후 return
    }

    /**
     * 복호화 처리
     *
     * @param cipherText 암호화된 데이터
     * @return 디코더된 데이터
     * @throws Exception
     */
    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);    // getInstance()에 Cipher 객체를 AES 암호화, CBC operation mode, PKCS5 padding scheme로 초기화
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");   // key 로 비밀키 생성

        /**
         * AES는 128비트(16바이트)단위로 암호화 하기때문에 IV 또한 16바이트 똑같은 크기여야 한다.
         * IV가 생성되면 이 값을 가지고 첫번째 블록을 암호화 하고, 매번 다른 IV를 생성하면 같은 평문이라도 다른 암호문을 생성할 수 있다.
         */
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());   // CBC 암호화는 IV 라는 벡터값이 하나가 더 들어간다고 한다.
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec); // cipher 객체를 복호화 모드로 초기화

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);   // 문자열을 다시 원래 형식으로 base64로 디코더
        byte[] decrypted = cipher.doFinal(decodedBytes);    // 복호화 진행해서 바이트화

        return new String(decrypted, StandardCharsets.UTF_8);   // 복호화 인코딩 후 return
    }
}
