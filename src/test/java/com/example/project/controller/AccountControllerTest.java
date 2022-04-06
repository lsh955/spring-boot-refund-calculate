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
import org.springframework.http.HttpHeaders;
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
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    private UserDto userDto(String userId,
                            String password,
                            String name,
                            String regNo) {
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
        String url = "/api/signup";
        doThrow(new CustomException(ErrorCode.UNABLE_TO_REG_NO))
                .when(accountServiceImp)
                .addSignup("2", "123", "이승환", "123456-789456");

        // when
        ResultActions resultActions = mockMvc.perform(
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
        String url = "/api/signup";
        doThrow(new CustomException(ErrorCode.REG_NO_OVERLAP))
                .when(accountServiceImp)
                .addSignup("2", "123", "이승환", "860824-1655068");

        // when
        ResultActions resultActions = mockMvc.perform(
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
        String url = "/api/signup";

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(userDto("2", "123", "이승환", "123456-789456")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인시 가입된 회원정보가 없을시")
    public void 로그인시_가입된_회원정보가_없을시() throws Exception {
        // given
        String url = "/api/login";
        doThrow(new CustomException(ErrorCode.MEMBER_NOT_FOUND))
                .when(accountServiceImp)
                .login("5555", "123");

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(userDto("5555", "123", "이승환", "123456-789456")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인시_등록된_패스워드가_틀렸을시")
    public void 로그인시_등록된_패스워드가_틀렸을시() throws Exception {
        // given
        String url = "/api/login";
        doThrow(new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD))
                .when(accountServiceImp)
                .login("1", "7979");

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(userDto("1", "7979", "홍길동", "860824-1655068")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 성공")
    public void 로그인_성공() throws Exception {
        // given
        String url = "/api/login";

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(userDto("1", "123", "홍길동", "860824-1655068")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("개인정보열람시_사용자의_데이터가_없을시")
    public void 개인정보열람시_사용자의_데이터가_없을시() throws Exception {
        // given
        String url = "/api/me";
        doThrow(new CustomException(ErrorCode.MEMBER_NOT_FOUND))
                .when(accountServiceImp)
                .readMember("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE");

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE"
                        )
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("개인정보열람 성공")
    public void 개인정보열람_성공() throws Exception {
        // given
        String url = "/api/me";

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(HttpHeaders.AUTHORIZATION,
                                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.uyIN2Sz88HOqUaa-M5th99uP-NIPsl2fI4ssgfkNPOs"
                        )
        );

        // then
        resultActions.andExpect(status().isOk());
    }
}
