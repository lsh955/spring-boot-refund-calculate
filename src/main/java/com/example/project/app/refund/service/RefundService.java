package com.example.project.app.refund.service;

import com.example.project.app.refund.dto.RefundDto;

/**
 * @author 이승환
 * @since 2022-03-14
 */
public interface RefundService {

    RefundDto getRefund(String token) throws Exception;
}
