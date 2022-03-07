package com.example.project.repository.scrap;

import com.example.project.controller.dto.ScrapDto;
import com.example.project.domain.scrap.ScrapOne;
import com.example.project.domain.scrap.ScrapOneRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ScrapOneRepositoryTest {

    @Autowired
    private ScrapOneRepository scrapOneRepository;

    @AfterEach
    public void cleanup() {
        this.scrapOneRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "SCRAP 001 등록")
    public void SCRAP_ONE_SAVE_TEST() {
        Long userIdx = 1L;

        String incomeDetails = "급여";
        Long totalPay = 24000000L;
        String startDate = "2020.10.03";
        String scrapCompany = "(주)활빈당";
        String payDate = "2020.11.02";
        String endDate = "2020.11.02";
        String incomeCate = "근로소득(연간)";
        String comNo = "012-34-56789";

        ScrapDto.ScrapOneDto scrapOneDto = ScrapDto.ScrapOneDto.builder()
                .incomeDetails("급여")
                .totalPay("24000000")
                .startDate("2020.10.03")
                .scrapCompany("(주)활빈당")
                .payDate("2020.11.02")
                .endDate("2020.11.02")
                .incomeCate("근로소득(연간)")
                .comNo("012-34-56789")
                .build();

        this.scrapOneRepository.save(scrapOneDto.toEntity(userIdx));

        List<ScrapOne> scrapOnes = scrapOneRepository.findAll();

        Assertions.assertThat(scrapOnes.get(0).getUserIdx()).isEqualTo(userIdx);
        Assertions.assertThat(scrapOnes.get(0).getIncomeDetails()).isEqualTo(incomeDetails);
        Assertions.assertThat(scrapOnes.get(0).getTotalPay()).isEqualTo(totalPay);
        Assertions.assertThat(scrapOnes.get(0).getStartDate()).isEqualTo(startDate);
        Assertions.assertThat(scrapOnes.get(0).getScrapCompany()).isEqualTo(scrapCompany);
        Assertions.assertThat(scrapOnes.get(0).getPayDate()).isEqualTo(payDate);
        Assertions.assertThat(scrapOnes.get(0).getEndDate()).isEqualTo(endDate);
        Assertions.assertThat(scrapOnes.get(0).getIncomeCate()).isEqualTo(incomeCate);
        Assertions.assertThat(scrapOnes.get(0).getComNo()).isEqualTo(comNo);
    }
}
