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
import com.example.project.app.refund.service.ScrapServiceImp;
import com.example.project.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private User user;

    @BeforeEach
    @DisplayName("초기 사용자정보 등록")
    public void beforeUserSave() {
        User result = User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();

        user = this.userRepository.save(result);
    }

    private User userBySave() {
        return User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();
    }
    
    @Test
    @DisplayName("사용자 정보가 있다면 정상적으로 불러오는가")
    public void 사용자_정보가_있다면_정상적으로_불러오는가() {
        // given
        String name = "홍길동";
        String regNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";

        doReturn(Optional.of(userBySave())).when(userRepository).findByNameAndRegNo(name, regNo);

        // when
        User result = scrapServiceImp.getUser(name, regNo);
        
        // then
        assertThat(result.getUserId()).isEqualTo("1");
        assertThat(result.getPassword()).isEqualTo("ELbbqFzaPvFZbCrhd61Mzw==");
        assertThat(result.getName()).isEqualTo("홍길동");
        assertThat(result.getRegNo()).isEqualTo("ldU2Z5ZlRuwPfYA1YfvOTw==");

        // verify
        verify(userRepository, times(1)).findByNameAndRegNo(name, regNo);
    }

    @Test
    @DisplayName("사용자 정보가 없다면 Exception 나타나는가")
    public void 사용자_정보가_없다면_Exception_나타나는가() {
        // given
        String name = "이승환";
        String regNo = "ldU2ZDD5ZlRuwPfYA1YfvOTw==";

        doReturn(Optional.empty()).when(userRepository).findByNameAndRegNo(name, regNo);

        // when
        CustomException result = assertThrows(CustomException.class,
                () -> scrapServiceImp.getUser(name, regNo)
        );

        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);

        // verify
        verify(userRepository, times(1)).findByNameAndRegNo(name, regNo);
    }

    @Test
    @DisplayName("가입한 유저의 회원정보 데이터가 존재하지 않을경우")
    public void 가입한_유저의_회원정보_데이터가_존재하지_않을경우() {
        // given
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("name", "이승환");
        tokenMap.put("regNo", "921108-1582816");
        tokenMap.put("exp", "1647749284");
        tokenMap.put("iat", "1647747484");

        JwtManager.TokenInfo tokenInfo = new JwtManager.TokenInfo("이승환", "U99p1DIkTEpARHoYcosMfA==", new Date(), new Date());

        doReturn(tokenInfo).when(jwtManager).getTokenInfo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE");
        doReturn(Optional.empty()).when(userRepository).findByNameAndRegNo(tokenMap.get("name"), "U99p1DIkTEpARHoYcosMfA==");

        // when
        CustomException result = assertThrows(CustomException.class,
                () -> scrapServiceImp.getSaveByScrap("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE")
        );

        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);

        // verify :: .description() 은 어떻게 나오는가 궁금해서 넣어봤다.
        verify(userRepository, times(1).description("한번 만 불어와야 한다")).findByNameAndRegNo(tokenMap.get("name"), "U99p1DIkTEpARHoYcosMfA==");
    }

//    @Test
//    @DisplayName("가입한 유저의 회원정보로 WebClient 요청이 이루어 지는가")
//    public void 가입한_유저의_회원정보로_WebClient_요청이_이루어_지는가() {
//        // given
//        String token = tokenByCreate();
//        HashMap<String, String> strToken = tokenByDecoder();
//        User user = userBySave();
//
//        doReturn(new JwtManager.TokenInfo("홍길동", "ldU2Z5ZlRuwPfYA1YfvOTw==", new Date(), new Date())).when(jwtManager).getTokenInfo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.uyIN2Sz88HOqUaa-M5th99uP-NIPsl2fI4ssgfkNPOs");
//        doReturn("ldU2Z5ZlRuwPfYA1YfvOTw==").when(aesCryptoUtil).encrypt("860824-1655068");
//        doReturn(Optional.of(user)).when(userRepository).findByNameAndRegNo(strToken.get("name"), "ldU2Z5ZlRuwPfYA1YfvOTw==");
//
//        doReturn(scrapDto()).when(scrapServiceImp).getClientScrap(new JwtManager.TokenInfo("홍길동", "ldU2Z5ZlRuwPfYA1YfvOTw==", new Date(), new Date()));
//
//        // when
//        ScrapDto result = scrapServiceImp.getSaveByScrap("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.uyIN2Sz88HOqUaa-M5th99uP-NIPsl2fI4ssgfkNPOs");
//
//        // then
//        assertThat(result.getAppVer()).isNotNull();
//        assertThat(result.getHostNm()).isNotNull();
//        assertThat(result.getWorkerResDt()).isNotNull();
//        assertThat(result.getWorkerReqDt()).isNotNull();
//    }
}