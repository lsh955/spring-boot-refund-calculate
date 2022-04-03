package com.example.project.service;

import com.example.project.app.account.domain.JoinAvailableRepository;
import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.account.dto.UserDto;
import com.example.project.app.account.service.AccountServiceImp;
import com.example.project.app.common.dto.JwtTokenDto;
import com.example.project.app.common.enums.ErrorCode;
import com.example.project.app.common.util.AESCryptoUtil;
import com.example.project.app.common.util.JwtManager;
import com.example.project.exception.CustomException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author 이승환
 * @since 2022-02-24
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceImpTest {

    private final String encryptedRegNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";
    private final String encryptedPassword = "ELbbqFzaPvFZbCrhd61Mzw==";
    private final String userId = "1";
    private final String decryptdPassword = "123";
    private final String name = "홍길동";
    private final String decryptdRegNo = "860824-1655068";

    @InjectMocks
    private AccountServiceImp accountServiceImp;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JoinAvailableRepository joinAvailableRepository;
    @Mock
    private AESCryptoUtil aesCryptoUtil;
    @Mock
    private JwtManager jwtManager;

    private User userBySave() {
        return User.builder()
                .userId("1")
                .password("ELbbqFzaPvFZbCrhd61Mzw==")
                .name("홍길동")
                .regNo("ldU2Z5ZlRuwPfYA1YfvOTw==")
                .build();
    }

    private String tokenByCreate() {
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6Ijg2MDgyNC0xNjU1MDY4IiwibmFtZSI6Iu2Zjeq4uOuPmSIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.uyIN2Sz88HOqUaa-M5th99uP-NIPsl2fI4ssgfkNPOs";
    }

    private JwtManager.TokenInfo tokenByDecoder() {
        return new JwtManager.TokenInfo("홍길동", "ldU2Z5ZlRuwPfYA1YfvOTw==", new Date(), new Date());
    }

    @Test
    @DisplayName("가입가능한 회원정보에 주민등록번호가 없을 시")
    public void RegNoOverlapFailureCheck() {
        // given
        doReturn(false).when(joinAvailableRepository).existsByRegNo("ldU2Z5ZlRuwDDPfYA1YfvOTw==");
        doReturn("ldU2Z5ZlRuwDDPfYA1YfvOTw==").when(aesCryptoUtil).encrypt(decryptdRegNo);

        // when
        final CustomException result = assertThrows(CustomException.class,
                () -> accountServiceImp.addSignup(userId, decryptdPassword, name, decryptdRegNo)
        );

        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.UNABLE_TO_REG_NO);

        // verify
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("가입한 회원정보에 주민증록번호가 있는지 중복체크")
    public void RegNoFailureCheck() throws Exception {
        // given
        doReturn(true).when(joinAvailableRepository).existsByRegNo(encryptedRegNo);
        doReturn(true).when(userRepository).existsByRegNo(encryptedRegNo);
        doReturn(encryptedRegNo).when(aesCryptoUtil).encrypt(decryptdRegNo);

        // when
        final CustomException result = assertThrows(CustomException.class,
                () -> accountServiceImp.addSignup(userId, decryptdPassword, name, decryptdRegNo)
        );

        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.REG_NO_OVERLAP);

        // verify
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 정상등록")
    public void addSignup() {
        // given
        doReturn(true).when(joinAvailableRepository).existsByRegNo(encryptedRegNo);
        doReturn(false).when(userRepository).existsByRegNo(encryptedRegNo);
        doReturn(encryptedRegNo).when(aesCryptoUtil).encrypt(decryptdRegNo);
        doReturn(userBySave()).when(userRepository).save(any(User.class));

        // when
        final UserDto result = accountServiceImp.addSignup(userId, decryptdPassword, name, decryptdRegNo);

        // then
        assertThat(result.getUserId()).isNotNull();

        // verify
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("로그인했을때 회원정보가 없을시")
    public void 로그인했을때_회원정보가_없을시() {
        // given
        doReturn(Optional.empty()).when(userRepository).findByUserId("2");

        // when
        final CustomException result = assertThrows(CustomException.class,
                () -> accountServiceImp.login("2", decryptdPassword)
        );

        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);

        // verify
        verify(userRepository, times(1)).findByUserId("2");
    }

    @Test
    @DisplayName("로그인했을때 입력된 패스워드가 틀려 검증이 실패했을시")
    public void 로그인했을때_패스워드_검증이_실패했을시() throws Exception {
        // given
        final User user = userBySave();
        doReturn(Optional.of(user)).when(userRepository).findByUserId(userId);
        doReturn(decryptdPassword).when(aesCryptoUtil).decrypt(userBySave().getPassword());

        // when
        final CustomException result = assertThrows(CustomException.class,
                () -> accountServiceImp.login(userId, "789")
        );

        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED_PASSWORD);

        // verify
        verify(userRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("로그인했을때 정상 토크발급")
    public void 로그인했을때_정상_토크발급() {
        // given
        final User user = userBySave();
        final String token = tokenByCreate();

        doReturn(Optional.of(user)).when(userRepository).findByUserId(userId);
        doReturn(decryptdPassword).when(aesCryptoUtil).decrypt(userBySave().getPassword());
        doReturn(token).when(jwtManager).generateToken(userBySave().getName(), userBySave().getRegNo());

        // when
        final JwtTokenDto result = accountServiceImp.login(userId, decryptdPassword);

        // then
        assertThat(result.getToken()).isNotNull();

        // verify
        verify(userRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("개인정보보기에서 가입된 유자가 없을시")
    public void 개인정보보기에서_가입된_유자가_없을시() throws Exception {
        // given
        final HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("name", "이승환");
        tokenMap.put("regNo", "921108-1582816");
        tokenMap.put("exp", "1647749284");
        tokenMap.put("iat", "1647747484");

        doReturn(new JwtManager.TokenInfo("이승환", "U99p1DIkTEpARHoYcosMfA==", new Date(), new Date())).when(jwtManager).getTokenInfo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE");
        doReturn(Optional.empty()).when(userRepository).findByNameAndRegNo(tokenMap.get("name"), "U99p1DIkTEpARHoYcosMfA==");

        // when
        final CustomException result = assertThrows(CustomException.class,
                () -> accountServiceImp.readMember("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjkyMTEwOC0xNTgyODE2IiwibmFtZSI6IuydtOyKue2ZmCIsImlhdCI6MTY0Nzc0NzQ4NCwiZXhwIjoxNjQ3NzQ5Mjg0fQ.TOtRqmykjAgPbtpNO5nMXrntVrdX2AFeG0Y2DINBagE")
        );

        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("개인정보보기에서 가입된 유자가 있을시")
    public void 개인정보보기에서_가입된_유자가_있을시() {
        // given
        final String token = tokenByCreate();
        final JwtManager.TokenInfo strToken = tokenByDecoder();
        final User user = userBySave();

        doReturn(strToken).when(jwtManager).getTokenInfo(token);
        doReturn(Optional.of(user)).when(userRepository).findByNameAndRegNo("홍길동", "ldU2Z5ZlRuwPfYA1YfvOTw==");

        // when
        final UserDto result = accountServiceImp.readMember(token);

        // then
        assertThat(result.getUserId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("홍길동");
        assertThat(result.getPassword()).isEqualTo("ELbbqFzaPvFZbCrhd61Mzw==");
        assertThat(result.getRegNo()).isEqualTo("ldU2Z5ZlRuwPfYA1YfvOTw==");
    }
}