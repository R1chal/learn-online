package com.richal.learnonline.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.api.LoginFeignAPI;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.Login;
import com.richal.learnonline.domain.User;
import com.richal.learnonline.domain.UserAccount;
import com.richal.learnonline.domain.UserBaseInfo;
import com.richal.learnonline.dto.CodeDto;
import com.richal.learnonline.dto.RegisterParamsDto;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.UserMapper;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.service.IUserAccountService;
import com.richal.learnonline.service.IUserBaseInfoService;
import com.richal.learnonline.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员登录账号 服务实现类
 *
 * @author Richal
 * @since 2025-07-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LoginFeignAPI loginFeignAPI;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private IUserBaseInfoService userBaseInfoService;

    @Override
    public void phoneRegister(RegisterParamsDto registerParamsDto) {
        String mobile = registerParamsDto.getMobile();
        String password = registerParamsDto.getPassword();
        Integer regChannel = registerParamsDto.getRegChannel();
        String smsCode = registerParamsDto.getSmsCode();
        if (StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(smsCode)
                || regChannel == null) {
            throw new GlobleBusinessException("必填参数为空");
        }

        String key = BusinessConstants.PHONE_REGISTER + mobile;
        Object object = redisTemplate.opsForValue().get(key);
        if (object == null) {
            throw new GlobleBusinessException("验证码过期");
        }
        CodeDto codeDto = (CodeDto) object;
        if (!codeDto.getCode().equals(smsCode)) {
            throw new GlobleBusinessException("验证码输入错误");
        }
        Wrapper<User> objectEntityWrapper = new EntityWrapper<>();
        objectEntityWrapper.eq("phone", mobile);
        User user = selectOne(objectEntityWrapper);
        if (user != null) {
            throw new GlobleBusinessException("该手机号已注册");
        }
        Login login = Login.builder()
                .username(mobile)
                .password(password)
                .type(1)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .build();
        JSONResult jsonResult = loginFeignAPI.register(login);
        Object data = jsonResult.getData();
        Login parseLogin = JSONObject.parseObject(JSONObject.toJSONString(data), Login.class);
        Long loginId = parseLogin.getId();

        long time = new Date().getTime();
        User userVO = User.builder()
                .createTime(time)
                .updateTime(time)
                .phone(mobile)
                .nickName(mobile)
                .bitState(0L)
                .secLevel(0)
                .loginId(loginId)
                .build();
        insert(userVO);

        UserAccount userAccount = UserAccount.builder()
                .id(loginId)
                .usableAmount(new BigDecimal(0))
                .frozenAmount(new BigDecimal(0))
                .createTime(time)
                .updateTime(time)
                .build();
        userAccountService.insert(userAccount);

        UserBaseInfo userBaseInfo = UserBaseInfo.builder()
                .id(loginId)
                .createTime(time)
                .updateTime(time)
                .regChannel(regChannel)
                .build();

        userBaseInfoService.insert(userBaseInfo);
        redisTemplate.delete(key);
    }
}
