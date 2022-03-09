package com.example.project.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이승환
 * @since 2022-03-09
 *
 * RefundService > getRefund 에서 return 할 용도
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundDto {

    private String name;
    private String taxCredit;
    private String taxAmount;
    private String refund;

    @Builder
    public RefundDto(String name,
                     String taxCredit,
                     String taxAmount,
                     String refund) {

        this.name = name;
        this.taxCredit = taxCredit;
        this.taxAmount = taxAmount;
        this.refund = refund;
    }
}
