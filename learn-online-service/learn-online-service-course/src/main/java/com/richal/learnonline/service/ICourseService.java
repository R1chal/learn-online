package com.richal.learnonline.service;

import com.richal.learnonline.domain.Course;
import com.baomidou.mybatisplus.service.IService;
import com.richal.learnonline.dto.CourseSaveDto;
import com.richal.learnonline.vo.CourseDetailVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Richal
 * @since 2025-07-22
 */
public interface ICourseService extends IService<Course> {

    void save(CourseSaveDto courseSaveDto);

    void onlineCourse(List<Long> courseIds);

    CourseDetailVO detail(Long courseId);
}
