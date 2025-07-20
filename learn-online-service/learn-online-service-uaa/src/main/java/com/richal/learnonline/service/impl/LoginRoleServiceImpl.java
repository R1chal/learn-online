package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.LoginRole;
import com.richal.learnonline.mapper.LoginRoleMapper;
import com.richal.learnonline.service.ILoginRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色中间表 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
@Service
public class LoginRoleServiceImpl extends ServiceImpl<LoginRoleMapper, LoginRole> implements ILoginRoleService {

}
