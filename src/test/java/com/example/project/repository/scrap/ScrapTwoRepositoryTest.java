package com.example.project.repository.scrap;

import com.example.project.controller.dto.ScrapDto;
import com.example.project.domain.scrap.ScrapTwo;
import com.example.project.domain.scrap.ScrapTwoRepository;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class ScrapTwoRepositoryTest {

    @Autowired
    private ScrapTwoRepository scrapTwoRepository;

    @After
    public void cleanup() {
        this.scrapTwoRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "SCRAP 002 등록")
    public void SCRAP_TWO_SAVE_TEST() throws Exception {
        Long userIdx = 1L;

        Long totalUsed = 2000000L;
        String taxAmount = "산출세액";

        ScrapDto.ScrapTwoDto scrapTwoDto = ScrapDto.ScrapTwoDto.builder()
                .totalUsed("2000000")
                .taxAmount("산출세액")
                .build();

        this.scrapTwoRepository.save(scrapTwoDto.toEntity(userIdx));

        List<ScrapTwo> scrapTwos = scrapTwoRepository.findAll();

        Assertions.assertThat(scrapTwos.get(0).getUserIdx()).isEqualTo(userIdx);
        Assertions.assertThat(scrapTwos.get(0).getTotalUsed()).isEqualTo(totalUsed);
        Assertions.assertThat(scrapTwos.get(0).getTaxAmount()).isEqualTo(taxAmount);
    }
}
