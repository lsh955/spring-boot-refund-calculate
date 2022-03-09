package com.example.project.service;

import com.example.project.controller.dto.ScrapDto;
import com.example.project.domain.account.User;
import com.example.project.domain.account.UserRepository;
import com.example.project.domain.scrap.ScrapListRepository;
import com.example.project.domain.scrap.ScrapOneRepository;
import com.example.project.domain.scrap.ScrapResponseRepository;
import com.example.project.domain.scrap.ScrapTwoRepository;
import com.example.project.enums.AccountStatus;
import com.example.project.util.AESCryptoUtil;
import com.example.project.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

/**
 * @author 이승환
 * @since 2022-02-20
 */
@Service
@RequiredArgsConstructor
public class ScrapService {

    private final UserRepository userRepository;
    private final ScrapListRepository scrapListRepository;
    private final ScrapOneRepository scrapOneRepository;
    private final ScrapTwoRepository scrapTwoRepository;
    private final ScrapResponseRepository scrapResponseRepository;

    private final AESCryptoUtil aesCryptoUtil;
    private final JwtTokenUtil jwtTokenUtil;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://codetest.3o3.co.kr")  // 공급자 BASE URL 지정
            .build();

    /**
     * 가입한 유저의 스크랩조회 및 저장
     *
     * @param token User Token
     * @return 스크랩조회 결과
     */
    @Transactional
    public Object getScrap(String token) throws Exception {
        // Token 검증
        HashMap<String, String> strToken = this.jwtTokenUtil.decoderToken(token);

        // 사용자 불러오기
        User user = this.userRepository.findByNameAndRegNo(
                strToken.get("name"),
                this.aesCryptoUtil.encrypt(strToken.get("regNo"))
        );

        ScrapDto scrapDto;
        if (user != null) {  // 가입된 정보가 있다면
            // 공급자로 부터의 데이터 조회
            scrapDto = webClient.post()
                    .uri("/scrap/")
                    .bodyValue(new JSONObject(strToken).toString())
                    .retrieve()
                    .bodyToMono(ScrapDto.class)
                    .block();
        } else {
            return AccountStatus.UNKNOWN;
        }

        // 데이터 리스트 결과저장
        this.scrapListRepository.save(scrapDto.getScrapListDto().toEntity(user.getUserIdx()));

        // scrap001 결과저장
        for (ScrapDto.ScrapOneDto scrapOneDto : scrapDto.getScrapListDto().getScrapOneDto())
            this.scrapOneRepository.save(scrapOneDto.toEntity(user.getUserIdx()));

        // scrap002 결과저장
        for (ScrapDto.ScrapTwoDto scrapTwoDto : scrapDto.getScrapListDto().getScrapTwoDto())
            this.scrapTwoRepository.save(scrapTwoDto.toEntity(user.getUserIdx()));

        // 응답결과 결과저장
        this.scrapResponseRepository.save(scrapDto.toEntity(user.getUserIdx()));

        return scrapDto;
    }
}
