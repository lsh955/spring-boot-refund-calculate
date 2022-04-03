package com.example.project.util;

import com.example.project.app.common.util.AESCryptoUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 이승환
 * @since 2022-03-08
 */
@SpringBootTest
public class AESCryptoUtilTest {

    @Autowired
    private AESCryptoUtil aesCryptoUtil;

    @Test
    @DisplayName("AES 암호화/복호화 테스트")
    public void AESUtilTest() {
        String userId = "lsh955";
        String name = "이승환";
        String regNo = "123456-789456";

        String encryptUserId = aesCryptoUtil.encrypt(userId);
        String encryptName = aesCryptoUtil.encrypt(name);
        String encryptRegNo = aesCryptoUtil.encrypt(regNo);

        String decryptUserId = aesCryptoUtil.decrypt(encryptUserId);
        String decryptName = aesCryptoUtil.decrypt(encryptName);
        String decryptRegNo = aesCryptoUtil.decrypt(encryptRegNo);

        assertThat(userId).isEqualTo(decryptUserId);
        assertThat(name).isEqualTo(decryptName);
        assertThat(regNo).isEqualTo(decryptRegNo);
    }
}
