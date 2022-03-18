package com.example.project.repository;

import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.refund.domain.ScrapResponse;
import com.example.project.app.refund.domain.ScrapResponseRepository;
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
public class ScrapResponseRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScrapResponseRepository scrapResponseRepository;

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
    @DisplayName("ScrapResponse 결과저장")
    public void ScrapResponseSave () {
        // given
        final ScrapDto scrapDtoResult = ScrapDto.builder()
                .appVer("2021112501")
                .hostNm("codetest")
                .workerResDt("2022-03-18T09:21:14.099003")
                .workerReqDt("2022-03-18T09:21:14.099326")
                .build();
            
        // when
        final ScrapResponse result = this.scrapResponseRepository.save(scrapDtoResult.toEntity(this.user));
            
        // then
        assertThat(result.getResultIdx()).isNotNull();
        assertThat(result.getUser().getUserId()).isNotNull();
        assertThat(result.getAppVer()).isEqualTo("2021112501");
        assertThat(result.getHostNm()).isEqualTo("codetest");
        assertThat(result.getWorkerResDt()).isEqualTo("2022-03-18T09:21:14.099003");
        assertThat(result.getWorkerReqDt()).isEqualTo("2022-03-18T09:21:14.099326");
    }
}
