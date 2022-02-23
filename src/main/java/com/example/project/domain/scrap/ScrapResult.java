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
@Table(name = "scrap_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultIdx; // 식별값

    @Column(name = "user_idx")
    private Long userIdx;

    private String errMsg;
    private String company;
    private String svcCd;

    private String appVer;
    private String hostNm;
    private String workerResDt;
    private String workerReqDt;

    @Builder
    public ScrapResult(String errMsg,
                       String company,
                       String svcCd,
                       String appVer,
                       String hostNm,
                       String workerResDt,
                       String workerReqDt,
                       Long userIdx) {

        this.errMsg = errMsg;
        this.company = company;
        this.svcCd = svcCd;
        this.appVer = appVer;
        this.hostNm = hostNm;
        this.workerResDt = workerResDt;
        this.workerReqDt = workerReqDt;
        this.userIdx = userIdx;
    }
}
