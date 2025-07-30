package com.richal.learnonline.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.domain.KillActivity;
import com.richal.learnonline.domain.KillCourse;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.KillCourseMapper;
import com.richal.learnonline.service.IKillActivityService;
import com.richal.learnonline.service.IKillCourseService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-30
 */
@Service
public class KillCourseServiceImpl extends ServiceImpl<KillCourseMapper, KillCourse> implements IKillCourseService {


    @Autowired
    private IKillActivityService killActivityService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void save(KillCourse killCourse) {
        KillActivity killActivityDB = killActivityService.selectById(killCourse.getActivityId());
        if(killActivityDB.getPublishStatus().equals(KillActivity.PUBLISH_STATUS_OK)) {
            throw new GlobleBusinessException("活动正在进行，稍后再试");
        }

        //校验
        Wrapper<KillCourse> ww = new EntityWrapper<>();
        ww.eq("activity_id", killCourse.getActivityId());
        ww.eq("course_id", killCourse.getCourseId());
        KillCourse killCourseDB = selectOne(ww);
        if(killCourseDB != null){
            throw new GlobleBusinessException("请勿重复添加已有课程");
        }

        // 2. 添加课程
        KillActivity killActivity = killActivityService.selectById(killCourse.getActivityId());
        
        killCourse.setKillLimit(1);
        killCourse.setKillSort(0);
        killCourse.setPublishStatus(KillCourse.PUBLISH_STATUS_WAIT);
        killCourse.setStartTime(killActivity.getBeginTime());
        killCourse.setEndTime(killActivity.getEndTime());
        killCourse.setCreateTime(new Date());
        killCourse.setTimeStr(killActivity.getTimeStr());
        
        insert(killCourse);
    }

    @Override
    public List<KillCourse> queryOnlineALL() {
        Set<Object> keys = redisTemplate.keys("activity_*");
        List<KillCourse> killCourses = new ArrayList<>();
        keys.forEach(key -> {
            List values = redisTemplate.opsForHash().values(key);
            killCourses.addAll(values);
        });
        return  killCourses;
    }
}
