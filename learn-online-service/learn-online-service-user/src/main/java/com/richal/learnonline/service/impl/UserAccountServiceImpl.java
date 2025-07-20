package com.richal.learnonline.service.impl;

import com.richal.learnonline.domain.UserAccount;
import com.richal.learnonline.mapper.UserAccountMapper;
import com.richal.learnonline.service.IUserAccountService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {

}
