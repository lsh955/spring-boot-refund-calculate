package com.example.project.controller;

import com.example.project.enums.AccountStatus;
import com.example.project.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
 * @since 2022-02-24
 */
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScrapControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName(value = "사용자정보 스크랩 요청성공(가입된 정보가 있을 시)")
    public void SCRAP_SUCCESS_TEST() throws Exception {
        String name = "홍길동";
        String regNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        HashMap<String, String> token = this.jwtTokenUtil.createToken(name, regNo);

        this.mockMvc.perform(post("http://localhost:8080/szs/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("jsonList.errMsg").value(""))
                .andExpect(jsonPath("jsonList.company").value("삼쩜삼"))
                .andExpect(jsonPath("jsonList.svcCd").value("test01"))
                .andExpect(jsonPath("jsonList.userId").value("1"))
                .andExpect(jsonPath("jsonList.scrap001[0].소득내역").value("급여"))
                .andExpect(jsonPath("jsonList.scrap001[0].총지급액").value("24000000"))
                .andExpect(jsonPath("jsonList.scrap001[0].업무시작일").value("2020.10.03"))
                .andExpect(jsonPath("jsonList.scrap001[0].기업명").value("(주)활빈당"))
                .andExpect(jsonPath("jsonList.scrap001[0].이름").value("홍길동"))
                .andExpect(jsonPath("jsonList.scrap001[0].지급일").value("2020.11.02"))
                .andExpect(jsonPath("jsonList.scrap001[0].업무종료일").value("2020.11.02"))
                .andExpect(jsonPath("jsonList.scrap001[0].주민등록번호").value("860824-1655068"))
                .andExpect(jsonPath("jsonList.scrap001[0].소득구분").value("근로소득(연간)"))
                .andExpect(jsonPath("jsonList.scrap001[0].사업자등록번호").value("012-34-56789"))
                .andExpect(jsonPath("jsonList.scrap002[0].총사용금액").value("2000000"))
                .andExpect(jsonPath("jsonList.scrap002[0].소득구분").value("산출세액"));
    }

    @Test
    @DisplayName(value = "사용자정보 스크랩 요청실패(가입된 정보가 없을 시)")
    public void SCRAP_FAILURE_TEST() throws Exception {
        String name = "이승환";
        String regNo = "eeXoFpR60+NfeIpj4aXnWw==";

        HashMap<String, String> token = this.jwtTokenUtil.createToken(name, regNo);

        this.mockMvc.perform(post("http://localhost:8080/szs/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(AccountStatus.UNKNOWN.getCode()))
                .andExpect(jsonPath("message").value(AccountStatus.UNKNOWN.getMessage()));
    }
}
