package com.richal.learnonline.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.domain.KillActivity;
import com.richal.learnonline.domain.KillCourse;
import com.richal.learnonline.dto.KillCourseDTO;
import com.richal.learnonline.dto.RedisOrderDTO;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.KillCourseMapper;
import com.richal.learnonline.service.IKillActivityService;
import com.richal.learnonline.service.IKillCourseService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.richal.learnonline.util.CodeGenerateUtils;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
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

    @Autowired
    private RedissonClient redissonClient;

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

    @Override
    public KillCourse queryOnlineOne(Long activityId, Long courseId) {
        Object object = redisTemplate.opsForHash().get("activity_" + activityId, "course:" + courseId);
        if(object != null){
            return  (KillCourse) object;
        }else {
            throw  new GlobleBusinessException("课程不存在");
        }
    }

    @Override
    public String kill(KillCourseDTO killCourseDTO) {
        //TODO假的登录
        Long loginId = 3L;
        //2.判断课程是否存在
        Object object = redisTemplate.opsForHash().get("activity_" + killCourseDTO.getActivityId(), "course:" + killCourseDTO.getCourseId());
        if(object == null){
            throw new GlobleBusinessException("课程不存在");
        }

        //3.判断时间是不是还在活动中
        KillCourse killCourse = (KillCourse) object;
        Date date = new Date();
        if (date.getTime() > killCourse.getEndTime()){
            throw new GlobleBusinessException("该课程秒杀活动结束");
        }

        //4.扣减库存
        String key = "activity_" + killCourse.getActivityId();
        RSemaphore semaphore = redissonClient.getSemaphore(key + ":" + killCourse.getCourseId());

        boolean acquire = semaphore.tryAcquire(1);
        if (!acquire) {
            throw new GlobleBusinessException("课程卖完了");
        }

        //5.创建订单
        RedisOrderDTO redisOrderDTO = new RedisOrderDTO();
        redisOrderDTO.setOrderNo(CodeGenerateUtils.generateOrderSn(3));
        redisOrderDTO.setTotalAmount(killCourse.getKillPrice());
        redisOrderDTO.setCourseId(killCourse.getCourseId());
        redisOrderDTO.setCoursePic(killCourse.getCoursePic());
        redisOrderDTO.setCourseName(killCourse.getCourseName());
        redisOrderDTO.setLoginId(loginId);

        redisTemplate.opsForValue().set("redisOrder:" + redisOrderDTO.getOrderNo(), redisOrderDTO);

        return  redisOrderDTO.getOrderNo();
    }
}
