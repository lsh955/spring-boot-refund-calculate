package com.example.project.controller;

import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.account.dto.UserDto;
import com.example.project.app.common.util.JwtTokenUtil;
import com.example.project.app.enums.AccountStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 이승환
 * @since 2022-02-19
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @AfterEach
    public void down() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "회원가입 등록성공(가입가능한, 최초 가입하는 경우)")
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

        this.mockMvc.perform(post("http://localhost:8080/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(AccountStatus.SIGNUP_SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(AccountStatus.SIGNUP_SUCCESS.getMessage()));
    }

    @Test
    @DisplayName(value = "회원가입 등록실패(화원가입이 이미 되어있는 경우)")
    public void SIGNUP_FAILURE_TEST() throws Exception {
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

        this.mockMvc.perform(post("http://localhost:8080/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(userDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName(value = "회원가입 등록실패(회원가입 가능한 유저가 아닌경우)")
    public void SIGNUP_UNABLE_FAILURE_TEST() throws Exception {
        String userId = "3";
        String password = "123";
        String name = "이승환";
        String regNo = "123456-789456";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();

        this.mockMvc.perform(post("http://localhost:8080/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(userDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName(value = "로그인 성공(올바른 정보로 로그인한 경우)")
    public void LOGIN_SUCCESS_TEST() throws Exception {
        String userId = "1";
        String password = "123";

        String name = "홍길동";
        String regNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";
        HashMap<String, String> token = this.jwtTokenUtil.createToken(name, regNo);

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password(password)
                .build();

        this.mockMvc.perform(post("http://localhost:8080/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value(token.get("token")));
    }

    @Test
    @DisplayName(value = "로그인 실패(올바른 정보로 로그인 하지 않는 경우)")
    public void LOGIN_FAILURE_TEST() throws Exception {
        String userId = "6";
        String password = "1234";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password(password)
                .build();

        this.mockMvc.perform(post("http://localhost:8080/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(userDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName(value = "개인정보 보기 성공")
    public void ME_SUCCESS_TEST() throws Exception {
        String name = "홍길동";
        String regNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        HashMap<String, String> token = this.jwtTokenUtil.createToken(name, regNo);

        this.mockMvc.perform(post("http://localhost:8080/szs/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(token)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName(value = "개인정보 보기 실패")
    public void ME_FAILURE_TEST() throws Exception {
        String name = "손오공";
        String regNo = "eeXoFpR60+NfeIpj4aXnWw==";

        HashMap<String, String> token = this.jwtTokenUtil.createToken(name, regNo);

        this.mockMvc.perform(post("http://localhost:8080/szs/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(token)))
                .andExpect(status().is4xxClientError());
    }
}
