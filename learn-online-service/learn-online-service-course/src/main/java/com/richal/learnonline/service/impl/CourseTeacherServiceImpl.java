package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.CourseTeacher;
import com.richal.learnonline.domain.Teacher;
import com.richal.learnonline.mapper.CourseTeacherMapper;
import com.richal.learnonline.service.ICourseTeacherService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 课程和老师的中间表 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-22
 */
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements ICourseTeacherService {


    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<Teacher> selectTeacherById(Long courseId) {
         return courseTeacherMapper.selectTeachersByCourseId(courseId);
    }
}
