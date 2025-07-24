package com.richal.learnonline.dto;

import com.richal.learnonline.domain.Course;
import com.richal.learnonline.domain.CourseDetail;
import com.richal.learnonline.domain.CourseMarket;
import com.richal.learnonline.domain.CourseResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSaveDto {

    @NotNull(message = "课程基本信息不能为空")
    @Valid  // 用于级联验证Course对象内的字段
    private Course course;

    @NotNull(message = "课程详情不能为空")
    @Valid
    private CourseDetail courseDetail;

    @NotNull(message = "课程营销信息不能为空")
    @Valid
    private CourseMarket courseMarket;

    private CourseResource courseResource;

    @NotEmpty(message = "教师不能为空")
    private List<Long> teacharIds;
}