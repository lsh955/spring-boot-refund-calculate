package com.example.project.controller;

import com.example.project.app.common.enums.ErrorCode;
import com.example.project.app.refund.controller.ScrapController;
import com.example.project.app.refund.service.ScrapServiceImp;
import com.example.project.exception.CustomException;
import com.example.project.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@ExtendWith(MockitoExtension.class)
class ScrapControllerTest {

    @InjectMocks
    private ScrapController scrapController;

    @Mock
    private ScrapServiceImp scrapServiceImp;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(scrapController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    @DisplayName(value = "사용자정보 스크랩 요청실패(가입된 정보가 없을 시)")
    public void SCRAP_FAILURE_TEST() throws Exception {
        // given
        String url = "/szs/scrap";
        doThrow(new CustomException(ErrorCode.MEMBER_NOT_FOUND))
                .when(scrapServiceImp)
                .getSaveByScrap("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6ImxkVTJaNVpsUnV3RERQZllBMVlmdk9Udz09IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.IwXAZqC2JqOZfHFXQg0cF2Fw9yYQou6y9YEeRDaRXOo");

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6ImxkVTJaNVpsUnV3RERQZllBMVlmdk9Udz09IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.IwXAZqC2JqOZfHFXQg0cF2Fw9yYQou6y9YEeRDaRXOo"
                        )
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }
}
