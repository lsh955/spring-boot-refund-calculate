package com.example.project.repository;

import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.refund.domain.ScrapTwo;
import com.example.project.app.refund.domain.ScrapTwoRepository;
import com.example.project.app.refund.dto.ScrapDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 이승환
 * @since 2022-03-18
 */
@DataJpaTest
public class ScrapTwoRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScrapTwoRepository scrapTwoRepository;

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
    @DisplayName("ScrapTwo 결과저장")
    public void ScrapTwoSave() {
        // given
        ScrapDto.ScrapTwoDto scrapTwoResult = ScrapDto.ScrapTwoDto.builder()
                .totalUsed("2000000")
                .taxAmount("산출세액")
                .build();

        // when
        ScrapTwo result = this.scrapTwoRepository.save(scrapTwoResult.toEntity(this.user));

        // then
        assertThat(result.getScrapTwoIdx()).isNotNull();
        assertThat(result.getUser().getUserIdx()).isNotNull();
        assertThat(result.getTotalUsed()).isEqualTo(2000000);
        assertThat(result.getTaxAmount()).isEqualTo("산출세액");
    }

    @Test
    @DisplayName("사용자 시퀀스값에 따른 총사용금액 불러오기")
    public void findByTotalUsed() {
        // given
        ScrapDto.ScrapTwoDto scrapTwoResult = ScrapDto.ScrapTwoDto.builder()
                .totalUsed("2000000")
                .taxAmount("산출세액")
                .build();

        // when
        this.scrapTwoRepository.save(scrapTwoResult.toEntity(this.user));
        Optional<Long> result = this.scrapTwoRepository.findByTotalUsed(this.user.getUserIdx());

        // then
        result.ifPresent(aLong -> assertThat(aLong).isEqualTo(2000000));
    }
}
