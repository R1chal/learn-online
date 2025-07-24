package com.richal.learnonline.controller;

import com.aliyuncs.exceptions.ClientException;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @PostMapping("/uploadFile")
    public JSONResult upload(@RequestParam("file") MultipartFile file) throws ClientException {
        String oploadFileUrl = ossService.oploadFile(file);
        return JSONResult.success(oploadFileUrl);
    }
}
