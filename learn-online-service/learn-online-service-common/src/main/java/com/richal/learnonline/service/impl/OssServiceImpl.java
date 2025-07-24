package com.richal.learnonline.service.impl;

import com.aliyun.oss.*;
import com.aliyun.oss.model.PutObjectResult;
import com.richal.learnonline.service.OssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.aliyuncs.exceptions.ClientException;

import java.io.*;

@Service
public class OssServiceImpl implements OssService {
    
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;
    
    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Override
    public String oploadFile(MultipartFile file) throws ClientException {
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        
        String fileUrl = "";
        try {
            // 使用原始文件名作为OSS对象名
            String objectName = file.getOriginalFilename();
            
            // 上传文件
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(file.getBytes()));
            
            // 构建文件访问URL
            fileUrl = "https://" + bucketName + "." + endpoint.replace("https://", "") + "/" + objectName;
            
            System.out.println("文件 " + objectName + " 上传成功。URL: " + fileUrl);
        } catch (OSSException oe) {
            System.out.println("OSS异常: " + oe.getErrorMessage());
            System.out.println("Error Code: " + oe.getErrorCode());
            System.out.println("Request ID: " + oe.getRequestId());
            throw new RuntimeException("OSS文件上传失败: " + oe.getErrorMessage());
        } catch (IOException e) {
            throw new RuntimeException("文件处理失败: " + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        
        return fileUrl;
    }
}
