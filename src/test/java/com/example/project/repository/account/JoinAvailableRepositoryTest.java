package com.example.project.repository.account;

import com.example.project.domain.account.JoinAvailableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class JoinAvailableRepositoryTest {

    @Autowired
    private JoinAvailableRepository joinAvailableRepository;

    @Test
    @DisplayName(value = "가입가능한 주민번호가 있다면 TRUE")
    public void EXISTS_BY_REGNO_TRUE_TEST() throws Exception {
        String regNoText = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        boolean regNo = this.joinAvailableRepository.existsByRegNo(regNoText);

        Assertions.assertThat(regNo).isEqualTo(true);
    }
}