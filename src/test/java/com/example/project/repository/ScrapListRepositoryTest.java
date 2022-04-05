package com.example.project.repository;

import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.refund.domain.ScrapList;
import com.example.project.app.refund.domain.ScrapListRepository;
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
public class ScrapListRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScrapListRepository scrapListRepository;

    private User user;

    @BeforeEach
    @DisplayName("초기 사용자정보 등록")
    public void beforeUserSave() {
        User result = User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();

        user = this.userRepository.save(result);
    }

    @Test
    @DisplayName("ScrapList 결과저장")
    public void ScrapListSave() {
        // given
        ScrapDto.ScrapListDto scrapListResult = ScrapDto.ScrapListDto.builder()
                .errMsg("errMsg")
                .company("홍당무")
                .svcCd("test01")
                .build();

        // when
        ScrapList result = this.scrapListRepository.save(scrapListResult.toEntity(this.user));

        // then
        assertThat(result.getResultIdx()).isNotNull();
        assertThat(result.getUser().getUserIdx()).isNotNull();
        assertThat(result.getErrMsg()).isEqualTo("errMsg");
        assertThat(result.getCompany()).isEqualTo("홍당무");
        assertThat(result.getSvcCd()).isEqualTo("test01");
    }
}
