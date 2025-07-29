package com.richal.learnonline.vo;

import com.richal.learnonline.domain.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CourseDetailVO {
    private Course course;
    private CourseMarket courseMarket;
    private List<CourseChapter> courseChapters;
    private List<Teacher> teachers;
    private CourseDetail courseDetail;
    private CourseSummary courseSummary;
}
