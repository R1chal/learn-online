package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.Permission;
import com.richal.learnonline.mapper.PermissionMapper;
import com.richal.learnonline.service.IPermissionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
