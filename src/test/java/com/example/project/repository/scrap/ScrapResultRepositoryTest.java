package com.example.project.repository.scrap;

import com.example.project.controller.dto.ScrapDto;
import com.example.project.domain.scrap.ScrapResult;
import com.example.project.domain.scrap.ScrapResultRepository;
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
class ScrapResultRepositoryTest {

    @Autowired
    private ScrapResultRepository scrapResultRepository;

    @After
    public void cleanup() {
        this.scrapResultRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "51561")
    public void SCRAP_RESULT_SAVE_TEST() throws Exception {
        Long userIdx = 1L;

        String errMsg = "null";
        String company = "삼쩜삼";
        String svcCd = "test01";
        String appVer = "2021112501";
        String hostNm = "jobis-codetest";
        String workerResDt = "2022-02-24T06:49:55.318457";
        String workerReqDt = "2022-02-24T06:49:55.318517";

        ScrapDto scrapDto = ScrapDto.builder()
                .appVer("2021112501")
                .hostNm("jobis-codetest")
                .workerReqDt("2022-02-24T06:49:55.318457")
                .workerResDt("2022-02-24T06:49:55.318517")
                .build();

        ScrapDto.ScrapListDto.builder()
                .errMsg("null")
                .company("삼쩜삼")
                .svcCd("test01")
                .build();

        this.scrapResultRepository.save(scrapDto.toEntity(userIdx));

        List<ScrapResult> scrapResults = scrapResultRepository.findAll();

        Assertions.assertThat(scrapResults.get(0)).isNotNull();
    }
}
