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
@Table(name = "scrap_One")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapOne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapOneIdx;   // 식별값

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    private String incomeDetails;
    private Long totalPay;
    private String startDate;
    private String scrapCompany;
    private String payDate;
    private String endDate;
    private String incomeCate;
    private String comNo;

    @Builder
    public ScrapOne(String incomeDetails,
                    Long totalPay,
                    String startDate,
                    String scrapCompany,
                    String payDate,
                    String endDate,
                    String incomeCate,
                    String comNo,
                    User user) {

        this.incomeDetails = incomeDetails;
        this.totalPay = totalPay;
        this.startDate = startDate;
        this.scrapCompany = scrapCompany;
        this.payDate = payDate;
        this.endDate = endDate;
        this.incomeCate = incomeCate;
        this.comNo = comNo;
        this.user = user;
    }
}
