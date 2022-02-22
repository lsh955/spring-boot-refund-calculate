package com.example.project.controller;

import com.example.project.controller.dto.UserDto;
import com.example.project.domain.account.User;
import com.example.project.domain.account.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 이승환
 * @since 2022-02-19
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @After
    public void down() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "사용자 회원가입")
    public void 사용자_회원가입() throws Exception {
        String url = "http://localhost:8080/szs/signup";

        String userId = "lshk955";
        String password = "123";
        String name = "홍길동";
        String regNo = "860824-1655068";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();

        this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        List<User> accountList = this.userRepository.findAll();
        assertThat(accountList.get(0).getUserId()).isEqualTo(userId);
        assertThat(accountList.get(0).getPassword()).isEqualTo(password);
        assertThat(accountList.get(0).getName()).isEqualTo(name);
        assertThat(accountList.get(0).getRegNo()).isEqualTo(regNo);
    }
}
