package com.example.project.service;

import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.common.dto.JwtTokenDto;
import com.example.project.app.common.enums.AccountStatus;
import com.example.project.app.refund.domain.ScrapOneRepository;
import com.example.project.app.refund.domain.ScrapResponseRepository;
import com.example.project.app.refund.domain.ScrapTwoRepository;
import com.example.project.app.refund.service.ScrapService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@SpringBootTest
class ScrapServiceImpTest {

    @Autowired
    private ScrapService scrapService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScrapOneRepository scrapOneRepository;
    @Autowired
    private ScrapTwoRepository scrapTwoRepository;
    @Autowired
    private ScrapResponseRepository scrapResponseRepository;

    @AfterEach
    public void cleanup() {
        scrapOneRepository.deleteAll();
        scrapTwoRepository.deleteAll();
        scrapResponseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "가입한 유저의 정보로 스크랩 성공")
    public void SCRAP_SUCCESS_TEST() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjEyMzQ1Ni03ODk0NTYiLCJuYW1lIjoi7J207Iq57ZmYIiwiaWF0IjoxNjQ1Njg1MzUyLCJleHAiOjE2NDU2ODcxNTJ9.fPSM4i2wBNyFnUGct8oToXXnCsTUhND7dB_DqIn_nB0";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        Object me = this.scrapService.getSaveByScrap(jwtTokenDto.getToken());

        assertThat(me).isNotNull();
    }

    @Test
    @DisplayName(value = "가입한 유저의 정보로 스크랩 실패")
    public void SCRAP_FAILURE_TEST() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjEyMzQ1Ni03ODk0NTYiLCJuYW1lIjoi7J207Iq57ZmYIiwiaWF0IjoxNjQ1Njg1MzUyLCJleHAiOjE2NDU2ODcxNTJ9.fPSM4i2wBNyFnUGct8oToXXnCsTUhND7dB_DqIn_nB0";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        Object me = this.scrapService.getSaveByScrap(jwtTokenDto.getToken());

        assertThat(me).isEqualTo(AccountStatus.UNKNOWN);
    }
}