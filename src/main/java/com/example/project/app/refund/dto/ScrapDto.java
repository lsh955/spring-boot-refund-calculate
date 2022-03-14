package com.example.project.app.refund.dto;

import com.example.project.app.account.domain.User;
import com.example.project.app.refund.domain.ScrapList;
import com.example.project.app.refund.domain.ScrapOne;
import com.example.project.app.refund.domain.ScrapResponse;
import com.example.project.app.refund.domain.ScrapTwo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 이승환
 * @since 2022-02-20
 *
 * 계층간 스크랩데이터 교환을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapDto {

    @JsonProperty("jsonList")
    private ScrapListDto scrapListDto;

    private String appVer;
    private String hostNm;
    private String workerResDt;
    private String workerReqDt;

    @Deprecated // 테스트 코드에서 사용하는 용도라 프로덕션 환경에서는 Deprecated
    @Builder
    public ScrapDto(String appVer,
                    String hostNm,
                    String workerResDt,
                    String workerReqDt) {

        this.appVer = appVer;
        this.hostNm = hostNm;
        this.workerResDt = workerResDt;
        this.workerReqDt = workerReqDt;
    }

    // DTO -> Entity
    public ScrapResponse toEntity(User user) {

        return ScrapResponse.builder()
                .appVer(getAppVer())
                .hostNm(getHostNm())
                .workerReqDt(getWorkerReqDt())
                .workerResDt(getWorkerReqDt())
                .user(user)
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ScrapListDto {

        @JsonProperty("scrap001")
        private List<ScrapOneDto> scrapOneDto;
        @JsonProperty("scrap002")
        private List<ScrapTwoDto> scrapTwoDto;

        private String errMsg;
        private String company;
        private String svcCd;
        private String userId;

        @Deprecated // 테스트 코드에서 사용하는 용도라 프로덕션 환경에서는 Deprecated
        @Builder
        public ScrapListDto(String errMsg,
                            String company,
                            String svcCd) {

            this.errMsg = errMsg;
            this.company = company;
            this.svcCd = svcCd;
        }

        // DTO -> Entity
        public ScrapList toEntity(User user) {

            return ScrapList.builder()
                    .errMsg(errMsg)
                    .company(company)
                    .svcCd(svcCd)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ScrapOneDto {

        @JsonProperty("소득내역")
        private String incomeDetails;
        @JsonProperty("총지급액")
        private String totalPay;
        @JsonProperty("업무시작일")
        private String startDate;
        @JsonProperty("기업명")
        private String scrapCompany;
        @JsonProperty("이름")
        private String name;
        @JsonProperty("지급일")
        private String payDate;
        @JsonProperty("업무종료일")
        private String endDate;
        @JsonProperty("주민등록번호")
        private String regNo;
        @JsonProperty("소득구분")
        private String incomeCate;
        @JsonProperty("사업자등록번호")
        private String comNo;

        @Deprecated // 테스트 코드에서 사용하는 용도라 프로덕션 환경에서는 Deprecated
        @Builder
        public ScrapOneDto(String incomeDetails,
                           String totalPay,
                           String startDate,
                           String scrapCompany,
                           String payDate,
                           String svcCd,
                           String endDate,
                           String incomeCate,
                           String comNo) {

            this.incomeDetails = incomeDetails;
            this.totalPay = totalPay;
            this.startDate = startDate;
            this.scrapCompany = scrapCompany;
            this.payDate = payDate;
            this.endDate = endDate;
            this.incomeCate = incomeCate;
            this.comNo = comNo;
        }

        public ScrapOne toEntity(User user) {

            return ScrapOne.builder()
                    .incomeDetails(incomeDetails)
                    .totalPay(Long.parseLong(totalPay))
                    .startDate(startDate)
                    .scrapCompany(scrapCompany)
                    .payDate(payDate)
                    .endDate(endDate)
                    .incomeCate(incomeCate)
                    .comNo(comNo)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ScrapTwoDto {

        @JsonProperty("총사용금액")
        private String totalUsed;
        @JsonProperty("소득구분")
        private String taxAmount;

        @Deprecated // 테스트 코드에서 사용하는 용도라 프로덕션 환경에서는 Deprecated
        @Builder
        public ScrapTwoDto(String totalUsed,
                           String taxAmount) {

            this.totalUsed = totalUsed;
            this.taxAmount = taxAmount;
        }

        // DTO -> Entity
        public ScrapTwo toEntity(User user) {

            return ScrapTwo.builder()
                    .totalUsed(Long.parseLong(totalUsed))
                    .taxAmount(taxAmount)
                    .user(user)
                    .build();
        }
    }
}
