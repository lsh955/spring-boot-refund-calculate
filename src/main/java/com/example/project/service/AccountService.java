package com.example.project.service;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.controller.dto.UserDto;
import com.example.project.domain.account.JoinAvailableRepository;
import com.example.project.domain.account.User;
import com.example.project.domain.account.UserRepository;
import com.example.project.enums.AccountStatus;
import com.example.project.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 이승환
 * @since 2022-02-18
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final JoinAvailableRepository joinAvailableRepository;

    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public Object signup(UserDto userDto) {
        // 가입가능한 사용자이름 체크
        if (!this.joinAvailableRepository.existsByName(userDto.getName()))
            return AccountStatus.UNABLE_TO_NAME;

        // 가입가능한 주민번호 체크
        if (!this.joinAvailableRepository.existsByRegNo(userDto.getRegNo()))
            return AccountStatus.UNABLE_TO_REG_NO;

        // 사용자이름 체크
        if (this.userRepository.existsByName(userDto.getName()))
            return AccountStatus.NAME_OVERLAP;

        // 주민번호 체크
        if (this.userRepository.existsByRegNo(userDto.getRegNo()))
            return AccountStatus.REG_NO_OVERLAP;

        // 사용자 등록
        this.userRepository.save(userDto.toEntity());

        return AccountStatus.SIGNUP_SUCCESS;
    }

    @Transactional
    public Object login(UserDto userDto) {
        // 아이디와 패스와드가 맞는지.
        if (this.userRepository.existsByUserId(userDto.getUserId()) && this.userRepository.existsByPassword(userDto.getPassword())) {
            User user = this.userRepository.findByUserId(userDto.getUserId());

            return this.jwtTokenUtil.createToken(user.getName(), user.getRegNo()); // 가입이 되었다면 토큰생성.
        } else {
            return AccountStatus.INCONSISTENT;  // 정보가 올바르지 않는다면.
        }
    }

    @Transactional
    public Object me(JwtTokenDto jwtTokenDto) {
        Object strToken = this.jwtTokenUtil.decoderToken(jwtTokenDto);

        JSONObject jsonObject = new JSONObject(strToken.toString());

        return this.userRepository.findByNameAndRegNo(jsonObject.get("name").toString(), jsonObject.get("regNo").toString());
    }
}
