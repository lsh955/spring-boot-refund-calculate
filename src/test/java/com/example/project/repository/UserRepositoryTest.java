package com.example.project.repository;

import com.example.project.app.account.domain.User;
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
        boolean isRegNo = this.userRepository.existsByRegNo(encryptByRegNo);

        // then
        assertThat(isRegNo).isFalse();
    }

    @Test
    @DisplayName("가입된 주민등록번호가 없을 시")
    public void isUserRegNoTrue () {
        // given
        String encryptByRegNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        // when
        boolean isRegNo = this.userRepository.existsByRegNo(encryptByRegNo);

        // then
        assertThat(isRegNo).isTrue();
    }

    @Test
    @DisplayName("사용자 등록")
    public void UserSave () {
        // given
        final User user = User.builder()
                .userId("2")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("김둘리")
                .regNo("U99p1DIkTEpARHoYcosMfA==")
                .build();

        // when
        final User result = this.userRepository.save(user);

        // then
        assertThat(result.getUserIdx()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("2");
        assertThat(result.getPassword()).isEqualTo("ELbbqFzaPvFZbCrhd61Mzw==");
        assertThat(result.getName()).isEqualTo("김둘리");
        assertThat(result.getRegNo()).isEqualTo("U99p1DIkTEpARHoYcosMfA==");
    }

    @Test
    @DisplayName("사용자 아이디 기준으로 데이터 불러오기")
    public void findByUserId () {
        // given
        final User user = User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();

        // when
        final User result = this.userRepository.findByUserId(user.getUserId());

        // then
        assertThat(result.getUserIdx()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("1");
        assertThat(result.getPassword()).isEqualTo("ELbbqFzaPvFZbCrhd61Mzw==");
        assertThat(result.getName()).isEqualTo("홍길동");
        assertThat(result.getRegNo()).isEqualTo("ldU2Z5ZlRuwPfYA1YfvOTw==");
    }
    
    @Test
    @DisplayName("사용자 이름과 주민번호에 따른 시퀀스값 불러오기")
    public void findByUserIdx () {
        // given
        final User user = User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();
            
        // when
        final Long result = this.userRepository.findByUserIdx(user.getName(), user.getRegNo());
            
        // then
        assertThat(result).isNotNull();
    }
}
