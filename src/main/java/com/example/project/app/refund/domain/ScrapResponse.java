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
@Table(name = "scrap_response")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultIdx; // 식별값

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    private String appVer;
    private String hostNm;
    private String workerResDt;
    private String workerReqDt;

    @Builder
    public ScrapResponse(String appVer,
                         String hostNm,
                         String workerResDt,
                         String workerReqDt,
                         User user) {

        this.appVer = appVer;
        this.hostNm = hostNm;
        this.workerResDt = workerResDt;
        this.workerReqDt = workerReqDt;
        this.user = user;
    }
}
