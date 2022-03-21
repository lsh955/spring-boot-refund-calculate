package com.example.project.service;

import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.common.enums.ErrorCode;
import com.example.project.app.common.util.AESCryptoUtil;
import com.example.project.app.common.util.JwtTokenUtil;
import com.example.project.app.refund.controller.ScrapController;
import com.example.project.app.refund.domain.ScrapListRepository;
import com.example.project.app.refund.domain.ScrapOneRepository;
import com.example.project.app.refund.domain.ScrapResponseRepository;
import com.example.project.app.refund.domain.ScrapTwoRepository;
import com.example.project.app.refund.service.ScrapServiceImp;
import com.example.project.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;


/**
 * @author 이승환
 * @since 2022-02-24
 */
@ExtendWith(MockitoExtension.class)
class ScrapServiceImpTest {

    @InjectMocks
    private ScrapController scrapController;

    @Mock
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
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private AESCryptoUtil aesCryptoUtil;
    
    @Test
    @DisplayName("")
    public void 가입한_유저의_회원정보_데이터가_존재하지_않을경우 () throws Exception {
        // given
        final HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("name", "이승환");
        tokenMap.put("regNo", "921108-1582816");
        tokenMap.put("exp", "1647749284");
        tokenMap.put("iat", "1647747484");

        doReturn(tokenMap).when(jwtTokenUtil).decoderToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE");
        doReturn("U99p1DIkTEpARHoYcosMfA==").when(aesCryptoUtil).encrypt(tokenMap.get("regNo"));
        doReturn(null).when(userRepository).findByNameAndRegNo(tokenMap.get("name"), "U99p1DIkTEpARHoYcosMfA==");

        // when
        final CustomException result = assertThrows(CustomException.class,
                () -> scrapServiceImp.getSaveByScrap("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE")
        );
            
        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }
}
