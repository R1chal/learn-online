package com.richal.learnonline.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.domain.CourseChapter;
import com.richal.learnonline.mapper.CourseChapterMapper;
import com.richal.learnonline.service.ICourseChapterService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程章节 ， 一个课程，多个章节，一个章节，多个视频 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-22
 */
@Service
public class CourseChapterServiceImpl extends ServiceImpl<CourseChapterMapper, CourseChapter> implements ICourseChapterService {

    @Override
    public List<CourseChapter> listByCourseId(long courseId) {
        Wrapper<CourseChapter> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("course_id", courseId);
        List<CourseChapter> courseChapters = selectList(entityWrapper);
        return courseChapters;
    }
}
