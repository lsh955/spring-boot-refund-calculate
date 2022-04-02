package com.example.project.app.refund.service;

import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.common.enums.ErrorCode;
import com.example.project.app.common.util.AESCryptoUtil;
import com.example.project.app.common.util.JwtTokenUtil;
import com.example.project.app.refund.domain.ScrapOneRepository;
import com.example.project.app.refund.domain.ScrapTwoRepository;
import com.example.project.app.refund.dto.RefundDto;
import com.example.project.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author 이승환
 * @since 2022-02-22
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefundServiceImp implements RefundService {

    private final UserRepository userRepository;
    private final ScrapOneRepository scrapOneRepository;
    private final ScrapTwoRepository scrapTwoRepository;

    private final AESCryptoUtil aesCryptoUtil;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 환급액 계산
     *
     * @param token User Token
     * @return      환금액 결과
     */
    @Override
    public RefundDto getRefund(final String token) throws Exception {
        // Token 검증
        final HashMap<String, String> strToken = this.jwtTokenUtil.decoderToken(token);

        // 주민등록번호 암호화
        final String encryptRegNo = this.aesCryptoUtil.encrypt(strToken.get("regNo"));

        // 사용자 불러오기
        final Long userIdx = getUserIdx(strToken.get("name"), encryptRegNo);

        // 총지급액 불러오기
        final Long totalPay = getFindByTotalPay(userIdx);
        // 총사용금액 불러오기
        final Long totalUsed = getFindByTotalUsed(userIdx);

        // 세액공제 한도계산
        final double taxCredit = getTaxCredit(totalPay);
        // 소득세액 공제계산
        final double taxAmount = getTaxAmount(totalUsed);

        final HashMap<String, Object> refunds = new HashMap<>();
        refunds.put("이름", strToken.get("name"));
        refunds.put("한도", taxCredit);
        refunds.put("공제액", taxAmount);
        refunds.put("환급액", Math.min(taxCredit, taxAmount));

        return RefundDto.builder()
                .name(refunds.get("이름").toString())
                .taxCredit(refunds.get("한도").toString())
                .taxAmount(refunds.get("공제액").toString())
                .refund(refunds.get("환급액").toString())
                .build();
    }

    /**
     * 사용자 시퀀스값 불러오기
     *
     * @param name  사용자 이름
     * @param regNo 주민등록번호
     * @return
     */
    public Long getUserIdx(final String name, final String regNo) {
        final Optional<Long> result = this.userRepository.findByUserIdx(name, regNo);

        return result.orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }

    /**
     * 총지급액 불러오기
     *
     * @param userIdx 사용자 키값
     * @return
     */
    public Long getFindByTotalPay(final Long userIdx) {
        final Optional<Long> result = scrapOneRepository.findByTotalPay(userIdx);

        return result.orElseThrow(() ->
                new CustomException(ErrorCode.NO_SCRAP_DATA)
        );
    }

    /**
     * 총사용금액 불러오기
     *
     * @param userIdx 사용자 키값
     * @return
     */
    public Long getFindByTotalUsed(final Long userIdx) {
        final Optional<Long> result = scrapTwoRepository.findByTotalUsed(userIdx);

        return result.orElseThrow(() ->
                new CustomException(ErrorCode.NO_SCRAP_DATA)
        );
    }

    /**
     * 세액공제 한도 계산
     *
     * @param totalPay  총급여액(총지급액)
     * @return          기준별 요건에 맞는 한도결과
     */
    public double getTaxCredit(final double totalPay) {
        double taxCredit = 0;   // 초기화

        // 3,300만원 이하 일 경우
        if (totalPay < 33000000)
            taxCredit = 740000; // 74만원 고정

        // 3,300만원 초과하고 7000만원 이하 일 경우
        if (totalPay > 33000000 && totalPay < 70000000) {
            taxCredit = 740000 - (totalPay * 0.008);    // 74만원 - [(총급여액 - 3,300만원) * 0.008]

            // 다만, 66만원보다 적은경우
            if (taxCredit < 660000)
                taxCredit = 660000; // 66만원 고정
        }

        // 7000만원 초과 일 경우
        if (totalPay > 70000000) {
            taxCredit = 660000 - ((totalPay - 70000000) * 1 / 2);   // 66만원 - [(총급여액 - 7,000만원) * 1/2]

            // 다만, 위 금액이 50만원보다 적을경우
            if (taxCredit < 500000)
                taxCredit = 500000; // 50만원 고정
        }

        return taxCredit;
    }

    /**
     * 소득세액 공제 계산
     *
     * @param totalUsed 산출세액
     * @return          기준별 요건에 맞는 공제결과
     */
    public double getTaxAmount(final double totalUsed) {
        double taxAmount = 0;   // 초기화

        // 130만원 이하 일 경우
        if (totalUsed < 1300000)
            taxAmount = totalUsed * 0.55;   // 산출세액의 100분의 55

        // 130만원 초과 일 경우
        if (totalUsed > 1300000)
            taxAmount = 715000 + (totalUsed * 0.30);    // 71만 5천원 + (130만원을 초과하는 금액의 100분의 30)

        return taxAmount;
    }
}
