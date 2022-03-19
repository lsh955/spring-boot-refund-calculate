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
}
