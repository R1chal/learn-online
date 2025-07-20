package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.UserAddress;
import com.richal.learnonline.mapper.UserAddressMapper;
import com.richal.learnonline.service.IUserAddressService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收货地址 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements IUserAddressService {

}
