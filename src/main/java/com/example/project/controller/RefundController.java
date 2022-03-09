package com.example.project.controller;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.service.RefundService;
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
 */
@RestController
@RequestMapping(value = "/szs", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RefundController {

    // 스크랩 정보를 바탕으로 한 환급액 계산
    private final RefundService refundService;

    /**
     * 환급액 계산
     *
     * @param jwtTokenDto   User Token
     * @return              환급액 결과
     */
    @ApiOperation(value = "환급액 계산", notes = "사용자정보를 기반으로 환급액 계산")
    @PostMapping(value = "/refund", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object refund(@RequestBody JwtTokenDto jwtTokenDto) throws Exception {

        return refundService.getRefund(
                jwtTokenDto.getToken()
        );
    }
}
