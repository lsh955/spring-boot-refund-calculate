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
@Table(name = "scrap_list")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultIdx; // 식별값

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    private String errMsg;
    private String company;
    private String svcCd;

    @Builder
    public ScrapList(String errMsg,
                     String company,
                     String svcCd,
                     User user) {

        this.errMsg = errMsg;
        this.company = company;
        this.svcCd = svcCd;
        this.user = user;
    }
}
