package com.example.project.controller;

import com.example.project.app.account.controller.AccountController;
import com.example.project.app.account.dto.UserDto;
import com.example.project.app.account.service.AccountServiceImp;
import com.example.project.app.common.enums.ErrorCode;
import com.example.project.exception.CustomException;
import com.example.project.exception.GlobalExceptionHandler;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 이승환
 * @since 2022-02-19
 */
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountServiceImp accountServiceImp;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    private void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    private UserDto userDto(String userId, String password, String name, String regNo) {
        return UserDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();
    }

    @Test
    @DisplayName("가입가능한 주민등록번호가 없을시")
    public void 가입가능한_주민등록번호가_없을시() throws Exception {
        // given
        final String url = "/szs/signup";
        doThrow(new CustomException(ErrorCode.UNABLE_TO_REG_NO))
                .when(accountServiceImp)
                .addSignup("2", "123", "이승환", "123456-789456");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(userDto("2", "123", "이승환", "123456-789456")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("가입된 주민등록번호가 이미 있을시")
    public void 가입된_주민등록번호가_이미_있을시() throws Exception {
        // given
        final String url = "/szs/signup";
        doThrow(new CustomException(ErrorCode.REG_NO_OVERLAP))
                .when(accountServiceImp)
                .addSignup("2", "123", "이승환", "860824-1655068");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(userDto("2", "123", "이승환", "860824-1655068")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원가입 성공")
    public void 회원가입_성공() throws Exception {
        // given
        final String url = "/szs/signup";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(userDto("2", "123", "이승환", "123456-789456")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk());
    }
}
