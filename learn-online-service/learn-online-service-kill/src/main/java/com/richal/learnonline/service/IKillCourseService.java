package com.richal.learnonline.service;

import com.richal.learnonline.domain.KillCourse;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Richal
 * @since 2025-07-30
 */
public interface IKillCourseService extends IService<KillCourse> {

    void save(KillCourse killCourse);

    List<KillCourse> queryOnlineALL();
}
