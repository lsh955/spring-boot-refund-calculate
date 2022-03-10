package com.example.project.domain.scrap;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author 이승환
 * @since 2022-02-21
 */
@Entity
@Getter
@Table(name = "scrap_two")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapTwo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long scrapTwoIdx;   // 식별값

    @Column(name = "user_idx")
    private Long userIdx;

    private Long totalUsed;
    private String taxAmount;

    @Builder
    public ScrapTwo(Long totalUsed,
                    String taxAmount,
                    Long userIdx) {

        this.totalUsed = totalUsed;
        this.taxAmount = taxAmount;
        this.userIdx = userIdx;
    }
}
