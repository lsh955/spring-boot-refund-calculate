package com.example.project.repository.account;

import com.example.project.controller.dto.UserDto;
import com.example.project.domain.account.User;
import com.example.project.domain.account.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "회원정보 등록")
    public void SAVE_TEST() throws Exception {
        String userId = "1";
        String name = "홍길동";
        String password = "ELbbqFzaPvFZbCrhd61Mzw==";
        String regNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .name(name)
                .password(password)
                .regNo(regNo)
                .build();

        this.userRepository.save(userDto.toEntity());

        List<User> userList = userRepository.findAll();

        Assertions.assertThat(userList.get(0).getUserId()).isEqualTo(userId);
        Assertions.assertThat(userList.get(0).getPassword()).isEqualTo(password);
        Assertions.assertThat(userList.get(0).getRegNo()).isEqualTo(regNo);
        Assertions.assertThat(userList.get(0).getName()).isEqualTo(name);
    }

    @Test
    @DisplayName(value = "아이디로 회원정보 불러오기")
    public void FIND_BY_USERID_TEST() throws Exception {
        String userId = "1";
        String name = "홍길동";
        String password = "ELbbqFzaPvFZbCrhd61Mzw==";
        String regNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        userRepository.findByUserId(userId);

        List<User> userList = userRepository.findAll();

        Assertions.assertThat(userList.get(0).getUserId()).isEqualTo(userId);
        Assertions.assertThat(userList.get(0).getPassword()).isEqualTo(password);
        Assertions.assertThat(userList.get(0).getRegNo()).isEqualTo(regNo);
        Assertions.assertThat(userList.get(0).getName()).isEqualTo(name);
    }

    @Test
    @DisplayName(value = "아이디가 있다면 TRUE")
    public void EXISTS_BY_USERID_TRUE_TEST() throws Exception {
        String userIdText = "1";

        boolean userId = userRepository.existsByUserId(userIdText);

        Assertions.assertThat(userId).isEqualTo(true);
    }

    @Test
    @DisplayName(value = "아이디가 있다면 FALSE")
    public void EXISTS_BY_USERID_FALSE_TEST() throws Exception {
        String userIdText = "lsh";

        boolean userId = userRepository.existsByUserId(userIdText);

        Assertions.assertThat(userId).isEqualTo(false);
    }

    @Test
    @DisplayName(value = "주민등록번호가 있다면 TRUE")
    public void EXISTS_BY_REGNO_TRUE_TEST() throws Exception {
        String regNoText = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        boolean regNo = userRepository.existsByRegNo(regNoText);

        Assertions.assertThat(regNo).isEqualTo(true);
    }

    @Test
    @DisplayName(value = "주민등록번호가 있다면 FALSE")
    public void EXISTS_BY_REGNO_FALSE_TEST() throws Exception {
        String regNoText = "eeXoFpR60+NfeIpj4aXnWw==";

        boolean regNo = userRepository.existsByRegNo(regNoText);

        Assertions.assertThat(regNo).isEqualTo(false);
    }

    @Test
    @DisplayName(value = "이름과 주민등록번호가 일치한 정보 불러오기")
    public void FIND_BY_NAME_AND_REGNO_TEST() throws Exception {
        String userId = "1";
        String name = "홍길동";
        String password = "ELbbqFzaPvFZbCrhd61Mzw==";
        String regNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        User user = userRepository.findByNameAndRegNo(name, regNo);

        Assertions.assertThat(user.getUserId()).isEqualTo(userId);
        Assertions.assertThat(user.getName()).isEqualTo(name);
        Assertions.assertThat(user.getPassword()).isEqualTo(password);
        Assertions.assertThat(user.getRegNo()).isEqualTo(regNo);
    }
}
