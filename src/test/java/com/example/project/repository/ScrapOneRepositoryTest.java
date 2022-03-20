package com.example.project.repository;

import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.refund.domain.ScrapOne;
import com.example.project.app.refund.domain.ScrapOneRepository;
import com.example.project.app.refund.dto.ScrapDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 이승환
 * @since 2022-03-18
 */
@DataJpaTest
public class ScrapOneRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScrapOneRepository scrapOneRepository;

    private User user;

    @BeforeEach
    @DisplayName("초기 사용자정보 등록")
    public void beforeUserSave() {
        final User result = User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();

        user = this.userRepository.save(result);
    }

    @Test
    @DisplayName("ScrapOne 결과저장")
    public void ScrapOneSave() {
        // given
        final ScrapDto.ScrapOneDto scrapOneResult = ScrapDto.ScrapOneDto.builder()
                .incomeDetails("급여")
                .totalPay("24000000")
                .startDate("2020.10.03")
                .scrapCompany("(주)활빈당")
                .payDate("2020.11.02")
                .endDate("2020.11.02")
                .incomeCate("근로소득(연간)")
                .comNo("012-34-56789")
                .build();

        // when
        final ScrapOne result = this.scrapOneRepository.save(scrapOneResult.toEntity(this.user));

        // then
        assertThat(result.getScrapOneIdx()).isNotNull();
        assertThat(result.getUser().getUserIdx()).isNotNull();
        assertThat(result.getIncomeDetails()).isEqualTo("급여");
        assertThat(result.getTotalPay()).isEqualTo(24000000);
        assertThat(result.getStartDate()).isEqualTo("2020.10.03");
        assertThat(result.getScrapCompany()).isEqualTo("(주)활빈당");
        assertThat(result.getPayDate()).isEqualTo("2020.11.02");
        assertThat(result.getEndDate()).isEqualTo("2020.11.02");
        assertThat(result.getIncomeCate()).isEqualTo("근로소득(연간)");
        assertThat(result.getComNo()).isEqualTo("012-34-56789");
    }

    @Test
    @DisplayName("사용자 시퀀스값에 따른 총지급액 불러오기")
    public void findByTotalPay() {
        // given
        final ScrapDto.ScrapOneDto scrapOneResult = ScrapDto.ScrapOneDto.builder()
                .incomeDetails("급여")
                .totalPay("24000000")
                .startDate("2020.10.03")
                .scrapCompany("(주)활빈당")
                .payDate("2020.11.02")
                .endDate("2020.11.02")
                .incomeCate("근로소득(연간)")
                .comNo("012-34-56789")
                .build();

        // when
        this.scrapOneRepository.save(scrapOneResult.toEntity(this.user));
        final Long totalPay = this.scrapOneRepository.findByTotalPay(this.user.getUserIdx());

        // then
        assertThat(totalPay).isEqualTo(24000000);
    }
}
