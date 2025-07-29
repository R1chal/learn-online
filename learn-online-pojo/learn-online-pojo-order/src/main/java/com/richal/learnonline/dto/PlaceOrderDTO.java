package com.richal.learnonline.dto;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderDTO {

    private List<Long> courseIds;
    private Integer payType;
    private Integer type;
    private String token;
}
