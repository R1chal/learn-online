package com.richal.learnonline.service.impl;

import com.richal.learnonline.SearchFeignAPI;
import com.richal.learnonline.doc.CourseDoc;
import com.richal.learnonline.domain.*;
import com.richal.learnonline.dto.CourseSaveDto;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.CourseMapper;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

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

    @Autowired
    private SearchFeignAPI searchFeignAPI;

    @Autowired
    private ICourseSummaryService courseSummaryService;

    @Override
    @Transactional
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

    @Override
    public void onlineCourse(List<Long> courseIds) {
        //1.参数校验
        if(CollectionUtils.isEmpty(courseIds)) {
            throw new GlobleBusinessException("请选择课程");
        }
        //2.课程是否上线
        List<Course> courses = selectBatchIds(courseIds);
        //3.上线
        courses.forEach(course -> {
            course.setStatus(Course.ONLINE);
            course.setOnlineTime(new Date());
        });
        updateBatchById(courses);

        try {
            //4.搜索服务
            List<CourseDoc> courseDocs = new ArrayList<>();
            courses.forEach(course -> {
                CourseDoc courseDoc = new CourseDoc();
                //属性拷贝
                BeanUtils.copyProperties(course, courseDoc);

                //营销信息
                CourseMarket courseMarket = courseMarketService.selectById(course.getId());
                if (courseMarket != null) {
                    BeanUtils.copyProperties(courseMarket, courseDoc);
                }

                //统计信息
                CourseSummary courseSummary = courseSummaryService.selectById(course.getId());
                if (courseSummary != null) {
                    BeanUtils.copyProperties(courseSummary, courseDoc);
                }
                
                courseDocs.add(courseDoc);
            });
            
            // 调用ES服务，如果失败不影响上线流程
            try {
                System.out.println("正在尝试将课程信息保存到搜索引擎，课程数量: " + courseDocs.size());
                // 打印第一个文档内容用于调试
                if(!courseDocs.isEmpty()) {
                    System.out.println("课程信息: " + courseDocs.get(0));
                }
                // 调用搜索服务
                JSONResult result = searchFeignAPI.save(courseDocs);
                System.out.println("搜索引擎保存结果: " + result);
            } catch (Exception e) {
                // 仅记录错误，不影响上线流程
                System.err.println("保存到搜索引擎失败，但课程已成功上线: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("课程上线过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            // 异常不会影响课程状态更新，课程仍然会上线
        }
    }
}
