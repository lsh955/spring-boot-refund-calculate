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
@Table(name = "scrap_list")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resultIdx; // 식별값

    @Column(name = "user_idx")
    private Long userIdx;

    private String errMsg;
    private String company;
    private String svcCd;

    @Builder
    public ScrapList(String errMsg,
                     String company,
                     String svcCd,
                     Long userIdx) {

        this.errMsg = errMsg;
        this.company = company;
        this.svcCd = svcCd;
        this.userIdx = userIdx;
    }
}
