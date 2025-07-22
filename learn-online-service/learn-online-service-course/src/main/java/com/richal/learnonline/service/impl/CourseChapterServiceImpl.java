package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.CourseChapter;
import com.richal.learnonline.mapper.CourseChapterMapper;
import com.richal.learnonline.service.ICourseChapterService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
