package com.example.project.repository;

import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.refund.domain.ScrapResponse;
import com.example.project.app.refund.domain.ScrapResponseRepository;
import com.example.project.app.refund.dto.ScrapDto;
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
public class ScrapResponseRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScrapResponseRepository scrapResponseRepository;

    @Test
    @DisplayName("ScrapResponse 결과저장")
    public void ScrapResponseSave () {
        // given
        final User user = User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();

        final ScrapDto scrapDtoResult = ScrapDto.builder()
                .appVer("2021112501")
                .hostNm("codetest")
                .workerResDt("2022-03-18T09:21:14.099003")
                .workerReqDt("2022-03-18T09:21:14.099326")
                .build();
            
        // when
        this.userRepository.save(user);
        final ScrapResponse result = this.scrapResponseRepository.save(scrapDtoResult.toEntity(user));
            
        // then
        assertThat(result.getResultIdx()).isNotNull();
        assertThat(result.getUser().getUserId()).isNotNull();
        assertThat(result.getAppVer()).isEqualTo("2021112501");
        assertThat(result.getHostNm()).isEqualTo("codetest");
        assertThat(result.getWorkerResDt()).isEqualTo("2022-03-18T09:21:14.099003");
        assertThat(result.getWorkerReqDt()).isEqualTo("2022-03-18T09:21:14.099326");
    }
}
