package com.example.project.app.refund.service;

import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.common.enums.ErrorCode;
import com.example.project.app.common.util.AESCryptoUtil;
import com.example.project.app.common.util.JwtManager;
import com.example.project.app.refund.domain.ScrapListRepository;
import com.example.project.app.refund.domain.ScrapOneRepository;
import com.example.project.app.refund.domain.ScrapResponseRepository;
import com.example.project.app.refund.domain.ScrapTwoRepository;
import com.example.project.app.refund.dto.ScrapDto;
import com.example.project.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

/**
 * @author 이승환
 * @since 2022-02-20
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapServiceImp implements ScrapService {

    private final UserRepository userRepository;
    private final ScrapListRepository scrapListRepository;
    private final ScrapOneRepository scrapOneRepository;
    private final ScrapTwoRepository scrapTwoRepository;
    private final ScrapResponseRepository scrapResponseRepository;

    private final JwtManager jwtManager;
    private final AESCryptoUtil aesCryptoUtil;

    private final WebClient webClient;

    /**
     * 가입한 유저의 스크랩조회 및 저장
     *
     * @param token User Token
     * @return 스크랩조회 결과
     */
    @Override
    public ScrapDto getSaveByScrap(final String token) throws Exception {
        // Token 검증
        final JwtManager.TokenInfo strToken = this.jwtManager.getTokenInfo(token);

        // 사용자 불러오기
        final User user = getUser(strToken.getName(), strToken.getRegNo());

        // 공급자로 부터의 데이터 조회
        final ScrapDto scrapDto = getClientScrap(strToken);

        saveScrapList(scrapDto, user);      // 리스트 결과저장
        saveScrapOne(scrapDto, user);       // scrap001 결과저장
        saveScrapTwo(scrapDto, user);       // scrap002 결과저장
        saveScrapResponse(scrapDto, user);  // 응답결과 결과저장

        return scrapDto;
    }

    /**
     * 공급자로 부터의 데이터 조회
     *
     * @param strToken
     * @return
     */
    public ScrapDto getClientScrap(final JwtManager.TokenInfo strToken) {
        String decryptRegNo = aesCryptoUtil.decrypt(strToken.getRegNo());

        JwtManager.TokenInfo result = JwtManager.TokenInfo.builder()
                .name(strToken.getName())
                .regNo(decryptRegNo)
                .issuedAt(strToken.getIssuedAt())
                .expire(strToken.getExpire())
                .build();

        return webClient.mutate().build()
                .post()
                .uri("https://codetest.3o3.co.kr/scrap/")
                .bodyValue(new JSONObject(result).toString())
                .retrieve() // memory leak 가능성 때문에 가급적 retrieve 를 사용하기를 권고
                .bodyToMono(ScrapDto.class)
                .block();
    }

    /**
     * 사용자 불러오기
     *
     * @param name
     * @param regNo
     * @return
     */
    public User getUser(final String name, final String regNo) {
        final Optional<User> result = this.userRepository.findByNameAndRegNo(name, regNo);

        return result.orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }

    /**
     * 데이터 리스트 결과저장
     *
     * @param scrapDto
     * @param user
     */
    @Transactional
    public void saveScrapList(final ScrapDto scrapDto, final User user) {
        this.scrapListRepository.save(scrapDto.getScrapListDto().toEntity(user));
    }

    /**
     * scrap001 결과저장
     *
     * @param scrapDto
     * @param user
     */
    @Transactional
    public void saveScrapOne(final ScrapDto scrapDto, final User user) {
        for (ScrapDto.ScrapOneDto result : scrapDto.getScrapListDto().getScrapOneDto())
            this.scrapOneRepository.save(result.toEntity(user));
    }

    /**
     * scrap002 결과저장
     *
     * @param scrapDto
     * @param user
     */
    @Transactional
    public void saveScrapTwo(final ScrapDto scrapDto, final User user) {
        for (ScrapDto.ScrapTwoDto result : scrapDto.getScrapListDto().getScrapTwoDto())
            this.scrapTwoRepository.save(result.toEntity(user));
    }

    /**
     * 응답결과 결과저장
     *
     * @param scrapDto
     * @param user
     */
    @Transactional
    public void saveScrapResponse(final ScrapDto scrapDto, final User user) {
        this.scrapResponseRepository.save(scrapDto.toEntity(user));
    }
}
