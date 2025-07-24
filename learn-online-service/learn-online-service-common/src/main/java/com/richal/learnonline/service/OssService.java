package com.richal.learnonline.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    String oploadFile(MultipartFile file) throws ClientException;
}
