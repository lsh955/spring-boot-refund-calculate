package com.example.project.repository.scrap;

import com.example.project.controller.dto.ScrapDto;
import com.example.project.domain.scrap.ScrapResponse;
import com.example.project.domain.scrap.ScrapResponseRepository;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
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
class ScrapResponseRepositoryTest {

    @Autowired
    private ScrapResponseRepository scrapResponseRepository;

    @AfterEach
    public void cleanup() {
        this.scrapResponseRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "스크랩 API 응답결과")
    public void SCRAP_RESULT_SAVE_TEST() throws Exception {
        Long userIdx = 1L;

        String appVer = "2021112501";
        String hostNm = "jobis-codetest";
        String workerResDt = "2022-02-24T06:49:55.318457";
        String workerReqDt = "2022-02-24T06:49:55.318517";

        ScrapDto scrapDto = ScrapDto.builder()
                .appVer(appVer)
                .hostNm(hostNm)
                .workerReqDt(workerResDt)
                .workerResDt(workerReqDt)
                .build();

        this.scrapResponseRepository.save(scrapDto.toEntity(userIdx));

        List<ScrapResponse> scrapResponses = scrapResponseRepository.findAll();

        Assertions.assertThat(scrapResponses.get(0)).isNotNull();
        Assertions.assertThat(scrapResponses.get(0).getUserIdx()).isEqualTo(userIdx);
        Assertions.assertThat(scrapResponses.get(0).getAppVer()).isEqualTo(appVer);
        Assertions.assertThat(scrapResponses.get(0).getHostNm()).isEqualTo(hostNm);
        Assertions.assertThat(scrapResponses.get(0).getWorkerReqDt()).isNotNull();
        Assertions.assertThat(scrapResponses.get(0).getWorkerReqDt()).isNotNull();
    }
}
