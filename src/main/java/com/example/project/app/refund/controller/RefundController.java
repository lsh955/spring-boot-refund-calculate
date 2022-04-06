package com.example.project.app.refund.controller;

import com.example.project.app.refund.dto.RefundDto;
import com.example.project.app.refund.service.RefundService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 이승환
 * @since 2022-02-20
 * <p>
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
     * @param token User Token
     * @return  환급액 결과
     */
    @ApiOperation(value = "환급액 계산", notes = "사용자정보를 기반으로 환급액 계산")
    @GetMapping(value = "/refund")
    public ResponseEntity<RefundDto> refund(@RequestHeader("Authorization") String token) {

        RefundDto result = refundService.getRefund(token);

        return ResponseEntity.ok(result);
    }
}
