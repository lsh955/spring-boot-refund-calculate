package com.example.project.controller.dto;

import com.example.project.domain.scrap.ScrapOne;
import com.example.project.domain.scrap.ScrapResult;
import com.example.project.domain.scrap.ScrapTwo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 이승환
 * @since 2022-02-20
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

    public ScrapResult toEntity(Long userIdx) {
        return ScrapResult.builder()
                .errMsg(scrapListDto.getErrMsg())
                .company(scrapListDto.getCompany())
                .svcCd(scrapListDto.getSvcCd())
                .appVer(getAppVer())
                .hostNm(getHostNm())
                .workerReqDt(getWorkerReqDt())
                .workerResDt(getWorkerReqDt())
                .userIdx(userIdx)
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

            public ScrapOne toEntity(Long userIdx) {
                return ScrapOne.builder()
                        .incomeDetails(incomeDetails)
                        .totalPay(Long.parseLong(totalPay))
                        .startDate(startDate)
                        .scrapCompany(scrapCompany)
                        .payDate(payDate)
                        .endDate(endDate)
                        .incomeCate(incomeCate)
                        .comNo(comNo)
                        .userIdx(userIdx)
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

            public ScrapTwo toEntity(Long userIdx) {
                return ScrapTwo.builder()
                        .totalUsed(Long.parseLong(totalUsed))
                        .taxAmount(taxAmount)
                        .userIdx(userIdx)
                        .build();
            }
        }
    }
}
