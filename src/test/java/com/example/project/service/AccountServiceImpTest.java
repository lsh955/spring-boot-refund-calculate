package com.example.project.service;

import com.example.project.app.account.domain.JoinAvailableRepository;
import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.account.service.AccountServiceImp;
import com.example.project.app.common.enums.AccountStatus;
import com.example.project.app.common.enums.ErrorCode;
import com.example.project.app.common.util.AESCryptoUtil;
import com.example.project.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private AccountServiceImp accountServiceImp;

    @Mock
    private UserRepository userRepository;
    @Mock
    private JoinAvailableRepository joinAvailableRepository;
    @Mock
    private AESCryptoUtil aesCryptoUtil;

    private final String encryptedRegNo = "ldU2Z5ZlRuwPfYA1YfvOTw==";
    private final String encryptedPassword = "ELbbqFzaPvFZbCrhd61Mzw==";

    private final String userId = "1";
    private final String password = "123";
    private final String name = "홍길동";
    private final String regNo = "860824-1655068";

    private User user() {
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

    @Test
    @DisplayName("가입가능한 회원정보에 주민등록번호가 없을 시")
    public void RegNoOverlapFailureCheck () throws Exception {
        // given
        doReturn(false).when(joinAvailableRepository).existsByRegNo("ldU2Z5ZlRuwDDPfYA1YfvOTw==");
        doReturn("ldU2Z5ZlRuwDDPfYA1YfvOTw==").when(aesCryptoUtil).encrypt(regNo);
            
        // when
        final CustomException result = assertThrows(CustomException.class,
                () -> accountServiceImp.addSignup(userId, password, name, regNo)
        );
            
        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.UNABLE_TO_REG_NO);

        // verify
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("가입한 회원정보에 주민증록번호가 있는지 중복체크")
    public void RegNoFailureCheck () throws Exception {
        // given
        doReturn(true).when(joinAvailableRepository).existsByRegNo(encryptedRegNo);
        doReturn(true).when(userRepository).existsByRegNo(encryptedRegNo);
        doReturn(encryptedRegNo).when(aesCryptoUtil).encrypt(regNo);

        // when
        final CustomException result = assertThrows(CustomException.class,
                () -> accountServiceImp.addSignup(userId, password, name, regNo)
        );

        // then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.REG_NO_OVERLAP);

        // verify
        verify(userRepository, times(0)).save(any(User.class));
    }
    
    @Test
    @DisplayName("회원가입 정상등록")
    public void addSignup () throws Exception {
        // given
        doReturn(true).when(joinAvailableRepository).existsByRegNo(encryptedRegNo);
        doReturn(false).when(userRepository).existsByRegNo(encryptedRegNo);
        doReturn(encryptedRegNo).when(aesCryptoUtil).encrypt(regNo);
        doReturn(user()).when(userRepository).save(any(User.class));
            
        // when
        final AccountStatus result = accountServiceImp.addSignup(userId, password, name, regNo);
            
        // then
        assertThat(result.getCode()).isEqualTo(AccountStatus.SIGNUP_SUCCESS.getCode());
        assertThat(result.getMessage()).isEqualTo(AccountStatus.SIGNUP_SUCCESS.getMessage());

        // verify
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("로그인했을때 회원정보가 없을시")
    public void 로그인했을때_회원정보가_없을시 () {
        // given
        doReturn(null).when(userRepository).findByUserId("2");

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
    public void 로그인했을때_패스워드_검증이_실패했을시 () throws Exception {
        // given
        final User user = userBySave();
        doReturn(user).when(userRepository).findByUserId(userId);
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
    public void 로그인했을때_정상_토크발급 () throws Exception {
        // given
        final User user = userBySave();
        final HashMap<String, String> tokenMap = tokenByCreate();

        doReturn(user).when(userRepository).findByUserId(userId);
        doReturn(decryptdPassword).when(aesCryptoUtil).decrypt(userBySave().getPassword());
        doReturn(tokenMap).when(jwtTokenUtil).createToken(userBySave().getName(), userBySave().getRegNo());
            
        // when
        final JwtTokenDto result = accountServiceImp.login(userId, decryptdPassword);
            
        // then
        assertThat(result.getToken()).isNotNull();

        // verify
        verify(userRepository, times(1)).findByUserId(userId);
    }
}
