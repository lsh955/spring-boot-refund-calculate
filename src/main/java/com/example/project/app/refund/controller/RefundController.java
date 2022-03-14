package com.example.project.app.refund.controller;

import com.example.project.app.common.dto.JwtTokenDto;
import com.example.project.app.refund.dto.RefundDto;
import com.example.project.app.refund.service.RefundService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 이승환
 * @since 2022-02-20
 *
 * 환급액에 관련된 Controller
 */
@RestController
@RequestMapping(value = "/szs", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    /**
     * 환급액 계산
     *
     * @param jwtTokenDto   User Token
     * @return              환급액 결과
     */
    @ApiOperation(value = "환급액 계산", notes = "사용자정보를 기반으로 환급액 계산")
    @PostMapping(value = "/refund", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RefundDto refund(@RequestBody JwtTokenDto jwtTokenDto) throws Exception {

        return refundService.getRefund(
                jwtTokenDto.getToken()
        );
    }
}
