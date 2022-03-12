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
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * @author 이승환
 * @since 2022-02-20
 */
@Slf4j
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
        if (user != null) {
            // 공급자로 부터의 데이터 조회
            scrapDto = webClient.post()
                    .uri("/scrap/")
                    .bodyValue(new JSONObject(strToken).toString())
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus != HttpStatus.OK,
                            clientResponse -> clientResponse.createException()
                                    .flatMap(it -> Mono.error(
                                            new RuntimeException("statusCode >> " + clientResponse.statusCode())
                                    )))
                    .bodyToMono(ScrapDto.class)
                    .onErrorResume(throwable -> Mono.error(
                            new RuntimeException(throwable)
                    ))
                    .block();
        } else {
            return AccountStatus.UNKNOWN;   // 가입된 정보가 없다면.
        }

        // 데이터 리스트 결과저장
        this.scrapListRepository.save(scrapDto.getScrapListDto().toEntity(user.getUserIdx()));  // DTO -> Entity

        // scrap001 결과저장
        for (ScrapDto.ScrapOneDto scrapOneDto : scrapDto.getScrapListDto().getScrapOneDto())
            this.scrapOneRepository.save(scrapOneDto.toEntity(user.getUserIdx()));  // DTO -> Entity

        // scrap002 결과저장
        for (ScrapDto.ScrapTwoDto scrapTwoDto : scrapDto.getScrapListDto().getScrapTwoDto())
            this.scrapTwoRepository.save(scrapTwoDto.toEntity(user.getUserIdx()));  // DTO -> Entity

        // 응답결과 결과저장
        this.scrapResponseRepository.save(scrapDto.toEntity(user.getUserIdx()));    // DTO -> Entity

        return scrapDto;
    }
}
