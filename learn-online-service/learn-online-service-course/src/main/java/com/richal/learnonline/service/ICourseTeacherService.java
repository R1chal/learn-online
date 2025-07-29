package com.richal.learnonline.service;

import com.richal.learnonline.domain.CourseTeacher;
import com.baomidou.mybatisplus.service.IService;
import com.richal.learnonline.domain.Teacher;

import java.util.List;

/**
 * <p>
 * 课程和老师的中间表 服务类
 * </p>
 *
 * @author Richal
 * @since 2025-07-22
 */
public interface ICourseTeacherService extends IService<CourseTeacher> {

    List<Teacher> selectTeacherById(Long courseId);
}
