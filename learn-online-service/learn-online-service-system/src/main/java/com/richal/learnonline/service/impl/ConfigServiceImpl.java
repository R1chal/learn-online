package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.Config;
import com.richal.learnonline.mapper.ConfigMapper;
import com.richal.learnonline.service.IConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-19
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
