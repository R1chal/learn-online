package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.OperationLog;
import com.richal.learnonline.mapper.OperationLogMapper;
import com.richal.learnonline.service.IOperationLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-19
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

}
