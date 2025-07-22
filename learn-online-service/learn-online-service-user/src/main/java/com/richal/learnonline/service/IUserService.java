package com.richal.learnonline.service;

import com.richal.learnonline.domain.User;
import com.baomidou.mybatisplus.service.IService;
import com.richal.learnonline.dto.RegisterParamsDto;

/**
 * <p>
 * 会员登录账号 服务类
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
public interface IUserService extends IService<User> {

    void phoneRegister(RegisterParamsDto registerParamsDto);
}
