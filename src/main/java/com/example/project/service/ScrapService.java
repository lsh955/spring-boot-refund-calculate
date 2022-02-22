package com.example.project.service;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.controller.dto.ScrapDto;
import com.example.project.domain.account.User;
import com.example.project.domain.account.UserRepository;
import com.example.project.domain.scrap.*;
import com.example.project.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author 이승환
 * @since 2022-02-20
 */
@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapOneRepository scrapOneRepository;
    private final ScrapTwoRepository scrapTwoRepository;
    private final ScrapResultRepository scrapResultRepository;
    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://codetest.3o3.co.kr")
            .build();

    public Object scrap(JwtTokenDto jwtTokenDto) {
        Object strToken = this.jwtTokenUtil.decoderToken(jwtTokenDto);
        JSONObject jsonObject = new JSONObject(strToken.toString());

        User user = this.userRepository.findByNameAndRegNo(jsonObject.get("name").toString(), jsonObject.get("regNo").toString());

        ScrapDto scrapDto = webClient.post()
                .uri("/scrap/")
                .bodyValue(strToken.toString())
                .retrieve()
                .bodyToMono(ScrapDto.class)
                .block();

        if (scrapDto != null) {
            // TODO :: 리펙토링 할것.
            this.scrapOneRepository.save(ScrapOne.builder()
                    .incomeDetails(scrapDto.getScrapListDto().getScrapOneDto().get(0).getIncomeDetails())
                    .totalPay(Long.parseLong(scrapDto.getScrapListDto().getScrapOneDto().get(0).getTotalPay()))
                    .startDate(scrapDto.getScrapListDto().getScrapOneDto().get(0).getStartDate())
                    .scrapCompany(scrapDto.getScrapListDto().getScrapOneDto().get(0).getScrapCompany())
                    .name(scrapDto.getScrapListDto().getScrapOneDto().get(0).getName())
                    .payDate(scrapDto.getScrapListDto().getScrapOneDto().get(0).getPayDate())
                    .endDate(scrapDto.getScrapListDto().getScrapOneDto().get(0).getEndDate())
                    .regNo(scrapDto.getScrapListDto().getScrapOneDto().get(0).getRegNo())
                    .incomeCate(scrapDto.getScrapListDto().getScrapOneDto().get(0).getIncomeCate())
                    .comNo(scrapDto.getScrapListDto().getScrapOneDto().get(0).getComNo())
                    .userIdx(user.getUserIdx())
                    .build());

            this.scrapTwoRepository.save(ScrapTwo.builder()
                    .totalUsed(Long.parseLong(scrapDto.getScrapListDto().getScrapTwoDto().get(0).getTotalUsed()))
                    .taxAmount(scrapDto.getScrapListDto().getScrapTwoDto().get(0).getTaxAmount())
                    .userIdx(user.getUserIdx())
                    .build());

            this.scrapResultRepository.save(ScrapResult.builder()
                    .errMsg(scrapDto.getScrapListDto().getErrMsg())
                    .company(scrapDto.getScrapListDto().getCompany())
                    .svcCd(scrapDto.getScrapListDto().getSvcCd())
                    .userId(scrapDto.getScrapListDto().getUserId())
                    .appVer(scrapDto.getAppVer())
                    .hostNm(scrapDto.getHostNm())
                    .workerResDt(scrapDto.getWorkerResDt())
                    .workerReqDt(scrapDto.getWorkerReqDt())
                    .userIdx(user.getUserIdx())
                    .build());
        }

        return scrapDto;
    }
}
