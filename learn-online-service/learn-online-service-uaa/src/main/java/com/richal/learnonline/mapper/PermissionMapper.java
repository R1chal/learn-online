package com.richal.learnonline.mapper;

import com.richal.learnonline.domain.Permission;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> queryPermissonByLoginId(Long id);
}
