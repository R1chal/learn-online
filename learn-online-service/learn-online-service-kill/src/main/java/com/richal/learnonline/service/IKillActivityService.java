package com.richal.learnonline.service;

import com.richal.learnonline.domain.KillActivity;
import com.baomidou.mybatisplus.service.IService;
import com.richal.learnonline.dto.KillActivityDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Richal
 * @since 2025-07-30
 */
public interface IKillActivityService extends IService<KillActivity> {

    void saveActivity(KillActivityDTO killActivityDTO);

    void publish(Long id);
}
