package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.Login;
import com.richal.learnonline.mapper.LoginMapper;
import com.richal.learnonline.service.ILoginService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录表 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {

}
