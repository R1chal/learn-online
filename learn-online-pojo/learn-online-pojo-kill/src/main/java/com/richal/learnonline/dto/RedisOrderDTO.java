package com.richal.learnonline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisOrderDTO {

    private String orderNo;
    private BigDecimal totalAmount;
    private Long loginId;
    private String courseName;
    private Long courseId;
    private String coursePic;
}
