package com.richal.learnonline.dto;

import com.richal.learnonline.domain.Course;
import com.richal.learnonline.domain.CourseMarket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    private Course course;
    private CourseMarket courseMarket;
}
