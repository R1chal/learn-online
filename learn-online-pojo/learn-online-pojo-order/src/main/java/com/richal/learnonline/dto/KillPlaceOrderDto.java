package com.richal.learnonline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KillPlaceOrderDto {

    private String orderNo;
    private String token;
    private Integer payType;
}
