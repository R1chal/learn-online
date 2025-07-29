package com.richal.learnonline.mapper;

import com.richal.learnonline.domain.CourseTeacher;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.richal.learnonline.domain.Teacher;

import java.util.List;

/**
 * <p>
 * 课程和老师的中间表 Mapper 接口
 * </p>
 *
 * @author Richal
 * @since 2025-07-22
 */
public interface CourseTeacherMapper extends BaseMapper<CourseTeacher> {

    List<Teacher> selectTeachersByCourseId(Long courseId);
}
