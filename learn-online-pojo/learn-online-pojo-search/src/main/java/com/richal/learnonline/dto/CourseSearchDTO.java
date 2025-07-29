package com.richal.learnonline.dto;

import com.richal.learnonline.query.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSearchDTO extends BaseQuery {

    private String chargeName;
    private Long courseTypeId;
    private String gradeName;
    private BigDecimal priceMax;
    private BigDecimal priceMin;

}
