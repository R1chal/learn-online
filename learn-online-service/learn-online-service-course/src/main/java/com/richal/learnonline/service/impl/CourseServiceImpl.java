package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.*;
import com.richal.learnonline.dto.CourseSaveDto;
import com.richal.learnonline.mapper.CourseMapper;
import com.richal.learnonline.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程服务实现类
 *
 * @author Richal
 * @since 2025-07-22
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Autowired
    ITeacherService teacherService;

    @Autowired
    ICourseDetailService courseDetailService;

    @Autowired
    ICourseMarketService courseMarketService;

    @Autowired
    ICourseResourceService courseResourceService;

    @Autowired
    ICourseTeacherService courseTeacherService;

    @Override
    public void save(CourseSaveDto courseSaveDto) {
        Course course = courseSaveDto.getCourse();
        CourseDetail courseDetail = courseSaveDto.getCourseDetail();
        CourseMarket courseMarket = courseSaveDto.getCourseMarket();
        CourseResource courseResource = courseSaveDto.getCourseResource();
        List<Long> teacherIds = courseSaveDto.getTeacharIds();

        //查询 name
        List<Teacher> teachers = teacherService.selectBatchIds(teacherIds);
        String teacherNames = teachers.stream().map(Teacher::getName).collect(Collectors.joining(","));
        course.setStatus(Course.OFFLINE);
        course.setTeacherNames(teacherNames);
        insert(course);

        courseDetail.setId(course.getId());
        courseDetailService.insert(courseDetail);

        courseMarket.setId(course.getId());
        courseMarketService.insert(courseMarket);

        // 确保CourseResource对象不为null
        if (courseResource == null) {
            courseResource = new CourseResource();
        }
        courseResource.setId(course.getId());
        courseResource.setCourseId(course.getId());
        // 检查courseResource对象属性是否正确设置
        System.out.println("CourseResource before insert: " + courseResource);
        courseResourceService.insert(courseResource);

        List<CourseTeacher> courseTeachers = new ArrayList<>();
        teachers.forEach(teacher -> {
            CourseTeacher courseTeacher = new CourseTeacher();
            courseTeacher.setCourseId(course.getId());
            courseTeacher.setTeacherId(teacher.getId());
            courseTeachers.add(courseTeacher);
        });
        courseTeacherService.insertBatch(courseTeachers);
    }
}
