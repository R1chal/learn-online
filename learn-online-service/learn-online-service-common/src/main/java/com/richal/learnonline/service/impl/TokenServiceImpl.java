package com.richal.learnonline.service.impl;

import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.service.TokenService;
import com.richal.learnonline.util.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public String getToken(Long courseId) {

        String token = StrUtils.getComplexRandomString(32);
        Long loginId = 3L;
        String key = BusinessConstants.REDIS_PREVENT_REPEAT_SUBMIT_ORDER + ":" +
                 loginId + ":" + courseId;
        redisTemplate.opsForValue().set(key, token, 5, TimeUnit.MINUTES);
        return token;
    }
}
