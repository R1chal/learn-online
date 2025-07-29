package com.richal.learnonline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfoDTO {

    private BigDecimal totalAmount;
    private List<CourseDTO> coursesDTOs;
}
