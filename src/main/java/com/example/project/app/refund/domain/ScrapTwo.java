package com.example.project.app.refund.domain;

import com.example.project.app.account.domain.User;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapTwoIdx;   // 식별값

    @ManyToOne
    @JoinColumn(name="user_idx")
    private User user;

    private Long totalUsed;
    private String taxAmount;

    @Builder
    public ScrapTwo(Long totalUsed,
                    String taxAmount,
                    User user) {

        this.totalUsed = totalUsed;
        this.taxAmount = taxAmount;
        this.user = user;
    }
}
