package com.example.project.service;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.controller.dto.UserDto;
import com.example.project.domain.account.UserRepository;
import com.example.project.enums.AccountStatus;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @After
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "유저생성 성공(가입가능하고 중복된 유저가 없을 시)")
    public void SIGNUP_SUCCESS_TEST() throws Exception {
        String userId = "2";
        String password = "123";
        String name = "김둘리";
        String regNo = "921108-1582816";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();

        Object signup = this.accountService.addSignup(userDto);

        Assertions.assertThat(signup).isEqualTo(AccountStatus.SIGNUP_SUCCESS);
    }

    @Test
    @DisplayName(value = "유저생성 실패(가입한 유저가 이미 있을경우)")
    public void SIGNUP_OVERLAP_FAILURE_TEST() throws Exception {
        String userId = "1";
        String password = "123";
        String name = "홍길동";
        String regNo = "860824-1655068";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();

        Object signup = this.accountService.addSignup(userDto);

        Assertions.assertThat(signup).isEqualTo(AccountStatus.REG_NO_OVERLAP);
    }

    @Test
    @DisplayName(value = "유저생성 실패(가입가능한 유저가 아닌경우)")
    public void SIGNUP_UNABLE_FAILURE_TEST() throws Exception {
        String userId = "10";
        String password = "123";
        String name = "이승환";
        String regNo = "123456-789456";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();

        Object signup = this.accountService.addSignup(userDto);

        Assertions.assertThat(signup).isEqualTo(AccountStatus.UNABLE_TO_REG_NO);
    }

    @Test
    @DisplayName(value = "로그인 성공(가입된 유저가 로그인 시)")
    public void LOGIN_SUCCESS_TEST() throws Exception {
        String userId = "1";
        String password = "123";
        String name = "홍길동";
        String regNo = "860824-1655068";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();

        Object login = this.accountService.login(userDto);

        Assertions.assertThat(login).isNotNull();
    }

    @Test
    @DisplayName(value = "로그인 실패(가입된 유저의 아이디가 맞는지)")
    public void LOGIN_FAILURE_TEST() throws Exception {
        String userId = "12";
        String password = "123";
        String name = "홍길동";
        String regNo = "860824-1655068";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();

        Object login = this.accountService.login(userDto);

        Assertions.assertThat(login).isEqualTo(AccountStatus.INCONSISTENT);
    }

    @Test
    @DisplayName(value = "개인정보 보기 성공(가입 한 유저인 경우)")
    public void ME_SUCCESS_TEST() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0NTY4NTM1MiwiZXhwIjoxNjQ1Njg3MTUyfQ.XG0jHD1-7rEujypfvOtHkhj6d91Fphry2F86_9jD5JA";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        Object me = this.accountService.readMember(jwtTokenDto);

        Assertions.assertThat(me).isNotNull();
    }

    @Test
    @DisplayName(value = "개인정보 보기 실패(가입하지 않는 유저인 경우)")
    public void ME_FAILURE_TEST() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjEyMzQ1Ni03ODk0NTYiLCJuYW1lIjoi7J207Iq57ZmYIiwiaWF0IjoxNjQ1Njg1MzUyLCJleHAiOjE2NDU2ODcxNTJ9.fPSM4i2wBNyFnUGct8oToXXnCsTUhND7dB_DqIn_nB0";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        Object me = this.accountService.readMember(jwtTokenDto);

        Assertions.assertThat(me).isEqualTo(AccountStatus.INCONSISTENT);
    }
}
