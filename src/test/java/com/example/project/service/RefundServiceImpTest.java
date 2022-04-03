package com.example.project.service;

import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.common.dto.JwtTokenDto;
import com.example.project.app.common.enums.AccountStatus;
import com.example.project.app.refund.domain.ScrapOneRepository;
import com.example.project.app.refund.domain.ScrapResponseRepository;
import com.example.project.app.refund.domain.ScrapTwoRepository;
import com.example.project.app.refund.service.RefundService;
import com.example.project.app.refund.service.ScrapService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@SpringBootTest
class RefundServiceImpTest {

    @Autowired
    private RefundService refundService;

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

    @BeforeEach
    public void setUp() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjEyMzQ1Ni03ODk0NTYiLCJuYW1lIjoi7J207Iq57ZmYIiwiaWF0IjoxNjQ1Njg1MzUyLCJleHAiOjE2NDU2ODcxNTJ9.fPSM4i2wBNyFnUGct8oToXXnCsTUhND7dB_DqIn_nB0";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        this.scrapService.getSaveByScrap(jwtTokenDto.getToken());
    }

    @AfterEach
    public void cleanup() {
        scrapOneRepository.deleteAll();
        scrapTwoRepository.deleteAll();
        scrapResponseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "환급액계산 성공(가입한 유저이며, 스크랩조회를 했을 시)")
    public void REFUND_SUCCESS_TEST() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0NTc3MjE1MCwiZXhwIjoxNjQ1NzczOTUwfQ.e-QnSPAsOvklv01reQYqFkcNGOdTmw-F2bx770GJkVs";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        Object me = this.refundService.getRefund(jwtTokenDto.getToken());

        assertThat(me).isNotNull();
    }

    @Test
    @DisplayName(value = "환급액계산 실패(스크랩정보가 없을 시)")
    public void REFUND_SCRAP_DATA_FAILURE_TEST() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0NTc3MjE1MCwiZXhwIjoxNjQ1NzczOTUwfQ.e-QnSPAsOvklv01reQYqFkcNGOdTmw-F2bx770GJkVs";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        Object me = this.refundService.getRefund(jwtTokenDto.getToken());

        assertThat(me).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName(value = "환급액계산 실패(유저의 정보가 없을 시)")
    public void REFUND_USER_DATA_FAILURE_TEST() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjEyMzQ1Ni03ODk0NTYiLCJuYW1lIjoi7J207Iq57ZmYIiwiaWF0IjoxNjQ1Njg1MzUyLCJleHAiOjE2NDU2ODcxNTJ9.fPSM4i2wBNyFnUGct8oToXXnCsTUhND7dB_DqIn_nB0";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        Object me = this.refundService.getRefund(jwtTokenDto.getToken());

        assertThat(me).isEqualTo(AccountStatus.UNKNOWN);
    }
}
