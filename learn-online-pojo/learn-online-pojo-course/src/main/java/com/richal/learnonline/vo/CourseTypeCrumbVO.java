package com.richal.learnonline.vo;

import com.richal.learnonline.domain.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseTypeCrumbVO {

    private CourseType courseType;
    private List<CourseType> courseTypeList;
}
