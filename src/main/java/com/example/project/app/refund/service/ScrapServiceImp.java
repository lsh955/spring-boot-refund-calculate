package com.example.project.app.refund.service;

import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.common.util.AESCryptoUtil;
import com.example.project.app.common.util.JwtTokenUtil;
import com.example.project.app.refund.domain.ScrapListRepository;
import com.example.project.app.refund.domain.ScrapOneRepository;
import com.example.project.app.refund.domain.ScrapResponseRepository;
import com.example.project.app.refund.domain.ScrapTwoRepository;
import com.example.project.app.refund.dto.ScrapDto;
import com.example.project.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static com.example.project.app.enums.ErrorCode.MEMBER_NOT_FOUND;

/**
 * @author 이승환
 * @since 2022-02-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapServiceImp implements ScrapService {

    private final UserRepository userRepository;
    private final ScrapListRepository scrapListRepository;
    private final ScrapOneRepository scrapOneRepository;
    private final ScrapTwoRepository scrapTwoRepository;
    private final ScrapResponseRepository scrapResponseRepository;

    private final AESCryptoUtil aesCryptoUtil;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 가입한 유저의 스크랩조회 및 저장
     *
     * @param token User Token
     * @return 스크랩조회 결과
     */
    @Transactional
    @Override
    public ScrapDto getSaveByScrap(String token) throws Exception {
        // Token 검증
        HashMap<String, String> strToken = this.jwtTokenUtil.decoderToken(token);

        // 주민등록번호 암호화
        String encryptRegNo = this.aesCryptoUtil.encrypt(strToken.get("regNo"));

        // 사용자 불러오기
        User user = getUser(strToken.get("name"), encryptRegNo);

        if (user == null)
            throw new CustomException(MEMBER_NOT_FOUND);

        // 공급자로 부터의 데이터 조회
        ScrapDto scrapDto = getClientScrap(strToken);

        saveScrapList(scrapDto, user);       // 리스트 결과저장
        saveScrapOne(scrapDto, user);        // scrap001 결과저장
        saveScrapTwo(scrapDto, user);        // scrap002 결과저장
        saveScrapResponse(scrapDto, user);   // 응답결과 결과저장

        return scrapDto;
    }


    /**
     * 공급자로 부터의 데이터 조회
     *
     * @param strToken
     * @return
     */
    private ScrapDto getClientScrap(HashMap<String, String> strToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://codetest.3o3.co.kr")  // 공급자 BASE URL 지정
                .build();

        return webClient.post()
                .uri("/scrap/")
                .bodyValue(new JSONObject(strToken).toString())
                .retrieve()
                .onStatus(httpStatus -> httpStatus != HttpStatus.OK,
                        clientResponse -> clientResponse.createException()
                                .flatMap(it -> Mono.error(
                                        new RuntimeException("statusCode >> " + clientResponse.statusCode())
                                ))
                )
                .bodyToMono(ScrapDto.class)
                .onErrorResume(throwable -> Mono.error(
                        new RuntimeException(throwable)
                ))
                .block();
    }

    /**
     * 사용자 불러오기
     *
     * @param name
     * @param regNo
     * @return
     */
    private User getUser(String name, String regNo) {
        return this.userRepository.findByNameAndRegNo(name, regNo);
    }

    /**
     * 데이터 리스트 결과저장
     *
     * @param scrapDto
     * @param user
     */
    private void saveScrapList(ScrapDto scrapDto, User user) {
        this.scrapListRepository.save(scrapDto.getScrapListDto().toEntity(user));
    }

    /**
     * scrap001 결과저장
     *
     * @param scrapDto
     * @param user
     */
    private void saveScrapOne(ScrapDto scrapDto, User user) {
        for (ScrapDto.ScrapOneDto scrapOneDtos : scrapDto.getScrapListDto().getScrapOneDto())
            this.scrapOneRepository.save(scrapOneDtos.toEntity(user));
    }

    /**
     * scrap002 결과저장
     *
     * @param scrapDto
     * @param user
     */
    private void saveScrapTwo(ScrapDto scrapDto, User user) {
        for (ScrapDto.ScrapTwoDto scrapTwoDtos : scrapDto.getScrapListDto().getScrapTwoDto())
            this.scrapTwoRepository.save(scrapTwoDtos.toEntity(user));
    }

    /**
     * 응답결과 결과저장
     *
     * @param scrapDto
     * @param user
     */
    private void saveScrapResponse(ScrapDto scrapDto, User user) {
        this.scrapResponseRepository.save(scrapDto.toEntity(user));
    }
}
