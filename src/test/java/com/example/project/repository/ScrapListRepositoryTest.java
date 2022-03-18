package com.example.project.repository;

import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.refund.domain.ScrapList;
import com.example.project.app.refund.domain.ScrapListRepository;
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
public class ScrapListRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScrapListRepository scrapListRepository;

    @Test
    @DisplayName("ScrapList 결과저장")
    public void ScrapListSave () {
        // given
        final User user = User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();

        final ScrapDto.ScrapListDto scrapListResult = ScrapDto.ScrapListDto.builder()
                .errMsg("errMsg")
                .company("company")
                .svcCd("svcCd")
                .build();

        // when
        this.userRepository.save(user);
        final ScrapList result = scrapListRepository.save(scrapListResult.toEntity(user));

        // then
        assertThat(result.getResultIdx()).isNotNull();
        assertThat(result.getUser().getUserIdx()).isNotNull();
        assertThat(result.getErrMsg()).isEqualTo("errMsg");
        assertThat(result.getCompany()).isEqualTo("company");
        assertThat(result.getSvcCd()).isEqualTo("svcCd");
    }
}
