package com.richal.learnonline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付申请DTO
 * 
 * @author Richal
 * @since 2025-07-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyDto {

    private String orderNo;
    
    private String callUrl;
    
    private Integer payType;
} 