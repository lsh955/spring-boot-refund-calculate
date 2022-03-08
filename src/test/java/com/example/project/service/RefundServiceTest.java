package com.example.project.service;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.domain.account.UserRepository;
import com.example.project.domain.scrap.ScrapOneRepository;
import com.example.project.domain.scrap.ScrapResponseRepository;
import com.example.project.domain.scrap.ScrapTwoRepository;
import com.example.project.enums.AccountStatus;
import com.example.project.enums.ScrapStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class RefundServiceTest {

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
    public void setUp() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjEyMzQ1Ni03ODk0NTYiLCJuYW1lIjoi7J207Iq57ZmYIiwiaWF0IjoxNjQ1Njg1MzUyLCJleHAiOjE2NDU2ODcxNTJ9.fPSM4i2wBNyFnUGct8oToXXnCsTUhND7dB_DqIn_nB0";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        this.scrapService.getScrap(jwtTokenDto);
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

        Object me = this.refundService.getRefund(jwtTokenDto);

        assertThat(me).isNotNull();
    }

    @Test
    @DisplayName(value = "환급액계산 실패(스크랩정보가 없을 시)")
    public void REFUND_SCRAP_DATA_FAILURE_TEST() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0NTc3MjE1MCwiZXhwIjoxNjQ1NzczOTUwfQ.e-QnSPAsOvklv01reQYqFkcNGOdTmw-F2bx770GJkVs";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        Object me = this.refundService.getRefund(jwtTokenDto);

        assertThat(me).isEqualTo(ScrapStatus.NO_SCRAP_DATA);
    }

    @Test
    @DisplayName(value = "환급액계산 실패(유저의 정보가 없을 시)")
    public void REFUND_USER_DATA_FAILURE_TEST() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjEyMzQ1Ni03ODk0NTYiLCJuYW1lIjoi7J207Iq57ZmYIiwiaWF0IjoxNjQ1Njg1MzUyLCJleHAiOjE2NDU2ODcxNTJ9.fPSM4i2wBNyFnUGct8oToXXnCsTUhND7dB_DqIn_nB0";

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .token(token)
                .build();

        Object me = this.refundService.getRefund(jwtTokenDto);

        assertThat(me).isEqualTo(AccountStatus.INCONSISTENT);
    }
}
