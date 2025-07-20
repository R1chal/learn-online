package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.LoginLog;
import com.richal.learnonline.mapper.LoginLogMapper;
import com.richal.learnonline.service.ILoginLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录记录 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

}
