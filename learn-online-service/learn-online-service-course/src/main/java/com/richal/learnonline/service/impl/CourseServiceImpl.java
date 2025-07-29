package com.richal.learnonline.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.SearchFeignAPI;
import com.richal.learnonline.api.MediaFeignAPI;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.doc.CourseDoc;
import com.richal.learnonline.domain.*;
import com.richal.learnonline.dto.*;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.CourseMapper;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.richal.learnonline.vo.CourseDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 课程服务实现类
 *
 * @author Richal
 * @since 2025-07-22
 */
@Slf4j
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

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private ICourseChapterService courseChapterService;

    @Autowired
    private MediaFeignAPI mediaFeignAPI;

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
            
            // 发送消息通知，如果失败不影响上线流程
            try {
                publishMessage();
            } catch (Exception e) {
                log.error("发送消息通知失败，但课程已成功上线: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            System.err.println("课程上线过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            // 异常不会影响课程状态更新，课程仍然会上线
        }
    }

    @Override
    public CourseDetailVO detail(Long courseId) {
        Course course = selectById(courseId);
        CourseMarket courseMarket = courseMarketService.selectById(courseId);
        CourseSummary courseSummary = courseSummaryService.selectById(courseId);
        CourseDetail courseDetail = courseDetailService.selectById(courseId);
        List<Teacher> courseTeachers = courseTeacherService.selectTeacherById(courseId);
        Wrapper<CourseChapter> ww = new EntityWrapper<>();
        ww.eq("course_id", courseId);
        List<CourseChapter> courseChapters = courseChapterService.selectList(ww);
        JSONResult jsonResult = mediaFeignAPI.selectMediaFileByCourseId(courseId);
        Object data = jsonResult.getData();
        String jsonString = JSONObject.toJSONString(data);
        List<MediaFile> mediaFiles = JSONObject.parseArray(jsonString, MediaFile.class);
        mediaFiles.forEach(mediaFile -> {
            courseChapters.forEach(courseChapter -> {
                if(mediaFile.getChapterId().longValue() == courseChapter.getId().longValue()) {
                    courseChapter.getMediaFiles().add(mediaFile);
                }
            });
        });
        CourseDetailVO courseDetailVO = new CourseDetailVO();
        courseDetailVO.setCourseDetail(courseDetail);
        courseDetailVO.setCourseSummary(courseSummary);
        courseDetailVO.setCourse(course);
        courseDetailVO.setCourseMarket(courseMarket);
        courseDetailVO.setCourseChapters(courseChapters);
        courseDetailVO.setTeachers(courseTeachers);
        return courseDetailVO;
    }

    @Override
    public CourseInfoDTO info(String courseId) {
        String[] split = courseId.split(",");
        List<Course> courses = selectBatchIds(Arrays.asList(split));
        List<CourseMarket> courseMarkets = courseMarketService.selectBatchIds(Arrays.asList(split));
        List<CourseDTO> courseDTOS = new ArrayList<>();
        
        // 使用Map提高查询效率
        Map<Long, Course> courseMap = courses.stream()
            .collect(Collectors.toMap(Course::getId, course -> course));
            
        // 匹配课程并创建DTO
        AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(new BigDecimal(0));
        
        courseMarkets.forEach(courseMarket -> {
            totalAmount.getAndUpdate(current -> current.add(courseMarket.getPrice()));
            Course course = courseMap.get(courseMarket.getId());
            if (course != null) {
                courseDTOS.add(new CourseDTO(course, courseMarket));
            }
        });
        
        CourseInfoDTO courseInfoDTO = new CourseInfoDTO();
        courseInfoDTO.setCoursesDTOs(courseDTOS);
        courseInfoDTO.setTotalAmount(totalAmount.get());
        return courseInfoDTO;
    }

    public void publishMessage(){
        try {
            //站内推送消息
            MessageStationDto messageStationDto = new MessageStationDto();
            messageStationDto.setTitle("课程发布");
            messageStationDto.setContent("尊敬的客户，课程已上线");
            messageStationDto.setType("系统消息");
            List<Long> userIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L);
            messageStationDto.setUserIds(userIds);
            
            try {
                SendResult stationResult = rocketMQTemplate.syncSend(BusinessConstants.ROCKETMQ_COURSE_TOPIC + ":"
                            + BusinessConstants.ROCKETMQ_COURSE_STATION_TAGS
                    , MessageBuilder.withPayload(JSONObject.toJSONString(messageStationDto)).build());
                log.info("站内消息发送结果：{}", stationResult);
            } catch (Exception e) {
                log.error("站内消息发送失败: {}", e.getMessage(), e);
            }

            //发送短信消息
            SMSDto smsDto = new SMSDto();
            smsDto.setContent("尊敬的客户，课程已上线");
            smsDto.setTitle("课程发布");
            List<UserPhoneDto> list = Arrays.asList(new UserPhoneDto("127.0.0.1", 1L, "13029768490"),
                    new UserPhoneDto("127.0.0.2", 2L, "13029768490"),
                    new UserPhoneDto("127.0.0.3", 3L, "13029168490"));
            smsDto.setUserPhoneDtos(list);
            
            try {
                SendResult SMSResult = rocketMQTemplate.syncSend(BusinessConstants.ROCKETMQ_COURSE_TOPIC + ":"
                            + BusinessConstants.ROCKETMQ_COURSE_SMS_TAGS
                    , MessageBuilder.withPayload(JSONObject.toJSONString(smsDto)).build());
                log.info("短信消息发送结果：{}", SMSResult);
            } catch (Exception e) {
                log.error("短信消息发送失败: {}", e.getMessage(), e);
            }

            //发送邮件
            EmailDto emailDto = new EmailDto();
            emailDto.setTitle("课程发布");
            emailDto.setContent("尊敬的客户，最新课程已经上线了");
            List<UserEmailDto> userEmailDtos = Arrays.asList(new UserEmailDto("123@gamil.com", 1L),
                    new UserEmailDto("125@gamil.com", 2L));
            emailDto.setUserEmailDto(userEmailDtos);
            
            try {
                SendResult emailResult = rocketMQTemplate.syncSend(BusinessConstants.ROCKETMQ_COURSE_TOPIC + ":"
                            + BusinessConstants.ROCKETMQ_COURSE_EMAIL_TAGS
                    , MessageBuilder.withPayload(JSONObject.toJSONString(emailDto)).build());
                log.info("邮件消息发送结果：{}", emailResult);
            } catch (Exception e) {
                log.error("邮件消息发送失败: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            // 捕获所有异常，确保不影响主流程
            log.error("消息发送过程中发生异常: {}", e.getMessage(), e);
        }
    }

}
