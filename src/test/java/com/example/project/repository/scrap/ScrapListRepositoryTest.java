package com.example.project.repository.scrap;

import com.example.project.controller.dto.ScrapDto;
import com.example.project.domain.scrap.ScrapList;
import com.example.project.domain.scrap.ScrapListRepository;
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
class ScrapListRepositoryTest {

    @Autowired
    private ScrapListRepository scrapListRepository;

    @After
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

        Assertions.assertThat(scrapResponses.get(0)).isNotNull();
    }
}
