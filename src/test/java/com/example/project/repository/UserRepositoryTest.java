package com.example.project.repository;

import com.example.project.app.account.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 이승환
 * @since 2022-03-18
 */
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("가입된 주민등록번호가 없을 시")
    public void isUserRegNoFalse () {
        // given
        String encryptByRegNo = "ldU2Z5ZlRuwDDDPfYA1YfvOTw==";

        // when
        boolean isRegNo = userRepository.existsByRegNo(encryptByRegNo);

        // then
        assertThat(isRegNo).isFalse();

    }

    @Test
    @DisplayName("가입된 주민등록번호가 없을 시")
    public void isUserRegNoTrue () {
        // given
        String encryptByRegNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        // when
        boolean isRegNo = userRepository.existsByRegNo(encryptByRegNo);

        // then
        assertThat(isRegNo).isTrue();
    }
}
