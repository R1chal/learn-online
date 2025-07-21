package com.richal.learnonline.service.impl;

import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.dto.CodeDto;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.service.VerifyCodeService;
import com.richal.learnonline.util.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 *
 * @author Richal
 * @since 2025/07/21
 */
@Slf4j
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void sendSmsCode(String phone) {

        if (StringUtils.isEmpty(phone)) {
            throw new GlobleBusinessException("手机号不能为空");
        }

        String key = BusinessConstants.PHONE_REGISTER + phone;
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            CodeDto codeDto = (CodeDto) object;
            if (new Date().getTime() - codeDto.getTimer() < 60 * 1000) {
                throw new GlobleBusinessException("请勿重复发送验证码");
            }
            log.info("你的验证码为：{}", codeDto.getCode());
            redisTemplate.opsForValue().set(key, codeDto, 5, TimeUnit.MINUTES);
        } else {
            String code = VerifyCodeUtils.generateVerifyCode(4);
            log.info("验证码为：{}", code);
            long timer = new Date().getTime();
            CodeDto value = new CodeDto(code, timer);
            redisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES);
        }
    }
}
