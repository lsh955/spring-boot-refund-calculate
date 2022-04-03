package com.example.project.service;

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
import com.example.project.app.refund.service.ScrapServiceImp;
import com.example.project.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


/**
 * @author 이승환
 * @since 2022-02-24
 */
@ExtendWith(MockitoExtension.class)
class ScrapServiceImpTest {

    @InjectMocks
    private ScrapServiceImp scrapServiceImp;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ScrapListRepository scrapListRepository;
    @Mock
    private ScrapOneRepository scrapOneRepository;
    @Mock
    private ScrapTwoRepository scrapTwoRepository;
    @Mock
    private ScrapResponseRepository scrapResponseRepository;

    @Mock
    private JwtManager jwtManager;
    @Mock
    private AESCryptoUtil aesCryptoUtil;

    private User userBySave() {
        return User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();
    }

    private HashMap<String, String> tokenByCreate() {
        final HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.uyIN2Sz88HOqUaa-M5th99uP-NIPsl2fI4ssgfkNPOs");

        return tokenMap;
    }

    private HashMap<String, String> tokenByDecoder() {
        final HashMap<String, String> result = new HashMap<>();
        result.put("name", "홍길동");
        result.put("regNo", "860824-1655068");
        result.put("exp", "1647749284");
        result.put("iat", "1647747484");

        return result;
    }

//    private ScrapDto scrapDto() {
//        return ScrapDto.builder()
//                .appVer("2021112501")
//                .hostNm("codetest")
//                .workerResDt("2022-03-26T01:39:25.327850")
//                .workerReqDt("2022-03-26T01:39:25.328166")
//                .build();
//    }
//
//    private ScrapList scrapListToEntity(User user) {
//        return ScrapList.builder()
//                .errMsg("errMsg")
//                .company("정관장")
//                .svcCd("test01")
//                .user(user)
//                .build();
//    }
//
//    private ScrapOne saveScrapOneToEntity(User user) {
//        return ScrapOne.builder()
//                .incomeDetails("급여")
//                .totalPay(24000000L)
//                .startDate("2020.10.03")
//                .scrapCompany("(주)활빈당")
//                .payDate("2020.11.02")
//                .endDate("2020.11.02")
//                .incomeCate("근로소득(연간)")
//                .comNo("012-34-56789")
//                .user(user)
//                .build();
//    }
//
//    private ScrapTwo saveScrapTwoToEntity(User user) {
//        return ScrapTwo.builder()
//                .totalUsed(2000000L)
//                .taxAmount("산출세액")
//                .user(user)
//                .build();
//    }
//
//    private ScrapResponse saveScrapResponseToEntity(User user) {
//        return ScrapResponse.builder()
//                .appVer("2021112501")
//                .hostNm("codetest")
//                .workerReqDt("2022-03-26T01:39:25.327850")
//                .workerResDt("2022-03-26T01:39:25.328166")
//                .user(user)
//                .build();
//    }

    @Test
    @DisplayName("가입한 유저의 회원정보 데이터가 존재하지 않을경우")
    public void 가입한_유저의_회원정보_데이터가_존재하지_않을경우() throws Exception {
        // given
        final HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("name", "이승환");
        tokenMap.put("regNo", "921108-1582816");
        tokenMap.put("exp", "1647749284");
        tokenMap.put("iat", "1647747484");

        JwtManager.TokenInfo tokenInfo = new JwtManager.TokenInfo("이승환", "U99p1DIkTEpARHoYcosMfA==", new Date(), new Date());

        doReturn(tokenInfo).when(jwtManager).getTokenInfo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE");
        doReturn(Optional.empty()).when(userRepository).findByNameAndRegNo(tokenMap.get("name"), "U99p1DIkTEpARHoYcosMfA==");

        // when
        final CustomException result = assertThrows(CustomException.class,
                () -> scrapServiceImp.getSaveByScrap("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE")
        );

        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);

        // verify :: .description() 은 어떻게 나오는가 궁금해서 넣어봤다.
        verify(userRepository, times(1).description("한번 만 불어와야 한다")).findByNameAndRegNo(tokenMap.get("name"), "U99p1DIkTEpARHoYcosMfA==");
    }

    @Test
    @DisplayName("가입한 유저의 회원정보로 WebClient 요청이 이루어 지는가")
    public void 가입한_유저의_회원정보로_WebClient_요청이_이루어_지는가 () throws Exception {
        // given
        final HashMap<String, String> tokenMap = tokenByCreate();
        final HashMap<String, String> strToken = tokenByDecoder();
        final User user = userBySave();

        doReturn(strToken).when(jwtManager).getTokenInfo(tokenMap.get("token"));
        doReturn("ldU2Z5ZlRuwPfYA1YfvOTw==").when(aesCryptoUtil).encrypt("860824-1655068");
        doReturn(Optional.of(user)).when(userRepository).findByNameAndRegNo(strToken.get("name"), "ldU2Z5ZlRuwPfYA1YfvOTw==");

        // TODO :: webClient Test 에 대해 더 자세히 알아봐야 겠다.
//        given(webClient.mutate().build()
//                .post()
//                .uri("https://codetest.3o3.co.kr/scrap/")
//                .bodyValue(new JSONObject(strToken).toString())
//                .retrieve()
//                .bodyToMono(ScrapDto.class)
//                .block()
//        ).willReturn(scrapDto());

        //doReturn(Optional.of(scrapDto())).when(scrapServiceImp).getClientScrap(strToken);

        // when
        ScrapDto result = scrapServiceImp.getSaveByScrap("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.uyIN2Sz88HOqUaa-M5th99uP-NIPsl2fI4ssgfkNPOs");

        // then
        assertThat(result.getAppVer()).isNotNull();
        assertThat(result.getHostNm()).isNotNull();
        assertThat(result.getWorkerResDt()).isNotNull();
        assertThat(result.getWorkerReqDt()).isNotNull();
    }
}