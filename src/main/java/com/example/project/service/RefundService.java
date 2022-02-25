package com.example.project.service;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.domain.account.User;
import com.example.project.domain.account.UserRepository;
import com.example.project.domain.scrap.ScrapOne;
import com.example.project.domain.scrap.ScrapOneRepository;
import com.example.project.domain.scrap.ScrapTwo;
import com.example.project.domain.scrap.ScrapTwoRepository;
import com.example.project.enums.AccountStatus;
import com.example.project.enums.ScrapStatus;
import com.example.project.util.AESCryptoUtil;
import com.example.project.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * @author 이승환
 * @since 2022-02-22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService {

    private final UserRepository userRepository;
    private final ScrapOneRepository scrapOneRepository;
    private final ScrapTwoRepository scrapTwoRepository;

    private final AESCryptoUtil aesCryptoUtil;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 환급액 계산
     *
     * @param jwtTokenDto User Token
     * @return 환금액 결과
     */
    public Object refund(JwtTokenDto jwtTokenDto) throws Exception {
        // Token 검증
        HashMap<String, String> strToken = this.jwtTokenUtil.decoderToken(jwtTokenDto);

        // 사용자 불러오기
        User user = this.userRepository.findByNameAndRegNo(strToken.get("name"), this.aesCryptoUtil.encrypt(strToken.get("regNo")));

        if (user == null)
            return AccountStatus.INCONSISTENT;

        // 스크랩정보 불러오기
        ScrapOne scrapOne = scrapOneRepository.findByUserIdx(user.getUserIdx());
        ScrapTwo scrapTwo = scrapTwoRepository.findByUserIdx(user.getUserIdx());

        if (scrapOne == null && scrapTwo == null)
            return ScrapStatus.NO_SCRAP_DATA;

        // 세액공제 한도계산
        double taxCredit = taxCredit(scrapOne.getTotalPay());
        // 소득세액 공제계산
        double taxAmount = taxAmount(scrapTwo.getTotalUsed());

        HashMap<String, Object> refunds = new HashMap<>();
        refunds.put("이름", user.getName());
        refunds.put("한도", unitConversion(taxCredit));
        refunds.put("공제액", unitConversion(taxAmount));
        refunds.put("환급액", unitConversion(Math.min(taxCredit, taxAmount)));

        return refunds;
    }

    /**
     * 세액공제 한도 계산
     *
     * @param totalPay 총급여액(총지급액)
     * @return 기준별 요건에 맞는 한도결과
     */
    public double taxCredit(double totalPay) {
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
     * @return 기준별 요건에 맞는 공제결과
     */
    public double taxAmount(double totalUsed) {
        double taxAmount = 0;   // 초기화

        // 130만원 이하 일 경우
        if (totalUsed < 1300000)
            taxAmount = totalUsed * 0.55;   // 산출세액의 100분의 55

        // 130만원 초과 일 경우
        if (totalUsed > 1300000)
            taxAmount = 715000 + (totalUsed * 0.30);    // 71만 5천원 + (130만원을 초과하는 금액의 100분의 30)

        return taxAmount;
    }

    /**
     * 금액단위 한글변환
     *
     * @param money 금액(ex:684000)
     * @return {단위변환결과}원
     */
    public String unitConversion(double money) {
        DecimalFormat d = new DecimalFormat("#,####");

        String[] han2 = {"", "십", "백", "천"};    // TODO :: 십,백,천 단위 추가하기.
        String[] han3 = {"", "만", "억", "조"};
        String[] str = d.format(money).split(",");

        StringBuilder result = new StringBuilder();
        int count = 0;
        for (int i = str.length; i > 0; i--) {
            if (Integer.parseInt(str[i - 1]) != 0)
                result.insert(0, Integer.parseInt(str[i - 1]) + han3[count]);

            count++;
        }

        return result + "원";
    }
}
