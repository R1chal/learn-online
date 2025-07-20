package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.User;
import com.richal.learnonline.mapper.UserMapper;
import com.richal.learnonline.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员登录账号 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
