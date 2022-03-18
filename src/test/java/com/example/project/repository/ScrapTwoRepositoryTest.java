package com.example.project.repository;

import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.refund.domain.ScrapTwo;
import com.example.project.app.refund.domain.ScrapTwoRepository;
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
public class ScrapTwoRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScrapTwoRepository scrapTwoRepository;
    
    @Test
    @DisplayName("ScrapTwo 결과저장")
    public void ScrapTwoSave () {
        // given
        final User user = User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();

        final ScrapDto.ScrapTwoDto scrapTwoResult = ScrapDto.ScrapTwoDto.builder()
                .totalUsed("2000000")
                .taxAmount("산출세액")
                .build();
            
        // when
        this.userRepository.save(user);
        final ScrapTwo result = scrapTwoRepository.save(scrapTwoResult.toEntity(user));
            
        // then
        assertThat(result.getScrapTwoIdx()).isNotNull();
        assertThat(result.getUser().getUserIdx()).isNotNull();
        assertThat(result.getTotalUsed()).isEqualTo(2000000);
        assertThat(result.getTaxAmount()).isEqualTo("산출세액");
    }
}
