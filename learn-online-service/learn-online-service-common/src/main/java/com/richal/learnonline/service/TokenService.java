package com.richal.learnonline.service;

import org.springframework.stereotype.Service;


public interface TokenService {
    String getToken(Long courseId);
}
