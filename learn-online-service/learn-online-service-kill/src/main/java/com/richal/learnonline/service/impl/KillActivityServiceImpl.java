package com.richal.learnonline.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.domain.KillActivity;
import com.richal.learnonline.domain.KillCourse;
import com.richal.learnonline.dto.KillActivityDTO;
import com.richal.learnonline.mapper.KillActivityMapper;
import com.richal.learnonline.service.IKillActivityService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.richal.learnonline.service.IKillCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-30
 */
@Service
public class KillActivityServiceImpl extends ServiceImpl<KillActivityMapper, KillActivity> implements IKillActivityService {


    @Autowired
    private IKillCourseService killCourseService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void saveActivity(KillActivityDTO killActivityDTO) {
        KillActivity killActivity = new KillActivity();
        killActivity.setName(killActivityDTO.getName());
        killActivity.setBeginTime(killActivityDTO.getBeginTime().getTime());
        killActivity.setEndTime(killActivityDTO.getEndTime().getTime());
        killActivity.setPublishStatus(KillActivity.PUBLISH_STATUS_WAIT);
        killActivity.setCreateTime(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(killActivityDTO.getBeginTime());
        killActivity.setTimeStr(str);
        insert(killActivity);
    }

    @Override
    public void publish(Long id) {

        KillActivity killActivity = selectById(id);
        killActivity.setPublishStatus(KillActivity.PUBLISH_STATUS_OK);
        updateById(killActivity);

        Wrapper<KillCourse> ww = new EntityWrapper();
        ww.eq("activity_id", id);
        List<KillCourse> killCourses = killCourseService.selectList(ww);

        killCourses.forEach(killCourse -> {
            killCourse.setPublishStatus(KillActivity.PUBLISH_STATUS_OK);
            killCourse.setPublishTime(new Date());
            killCourseService.updateById(killCourse);

            String key = "activity_" + id;
            redisTemplate.opsForHash().put(key, "课程：" + killCourse.getCourseId(), killCourse);
            redisTemplate.opsForValue().set("course:" + killCourse.getCourseId(), killCourse.getKillCount());
        });

    }
}
