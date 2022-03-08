package com.example.project.repository.scrap;

import com.example.project.controller.dto.ScrapDto;
import com.example.project.domain.scrap.ScrapList;
import com.example.project.domain.scrap.ScrapListRepository;
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
class ScrapListRepositoryTest {

    @Autowired
    private ScrapListRepository scrapListRepository;

    @AfterEach
    public void cleanup() {
        this.scrapListRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "json List 응답결과")
    public void SCRAP_RESULT_SAVE_TEST() throws Exception {
        Long userIdx = 1L;


        String errMsg = "2021112501";
        String company = "삼쩜삼";
        String svcCd = "test01";

        ScrapDto.ScrapListDto scrapDto = ScrapDto.ScrapListDto.builder()
                .errMsg(errMsg)
                .company(company)
                .svcCd(svcCd)
                .build();

        this.scrapListRepository.save(scrapDto.toEntity(userIdx));

        List<ScrapList> scrapResponses = scrapListRepository.findAll();

        assertThat(scrapResponses.get(0)).isNotNull();
    }
}
