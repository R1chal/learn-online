package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.Employee;
import com.richal.learnonline.mapper.EmployeeMapper;
import com.richal.learnonline.service.IEmployeeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-19
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
