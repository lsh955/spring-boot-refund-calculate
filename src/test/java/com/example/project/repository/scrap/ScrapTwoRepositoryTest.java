package com.example.project.repository.scrap;

import com.example.project.controller.dto.ScrapDto;
import com.example.project.domain.scrap.ScrapTwo;
import com.example.project.domain.scrap.ScrapTwoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@SpringBootTest
class ScrapTwoRepositoryTest {

    @Autowired
    private ScrapTwoRepository scrapTwoRepository;

    @AfterEach
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

        assertThat(scrapTwos.get(0).getUserIdx()).isEqualTo(userIdx);
        assertThat(scrapTwos.get(0).getTotalUsed()).isEqualTo(totalUsed);
        assertThat(scrapTwos.get(0).getTaxAmount()).isEqualTo(taxAmount);
    }
}
