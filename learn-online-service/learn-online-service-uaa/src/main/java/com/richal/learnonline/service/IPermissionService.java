package com.richal.learnonline.service;

import com.richal.learnonline.domain.Permission;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
public interface IPermissionService extends IService<Permission> {

    List<Permission> queryPermissonByLoginId(Long id);
}
