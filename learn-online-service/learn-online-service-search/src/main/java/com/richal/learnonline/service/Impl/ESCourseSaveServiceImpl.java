package com.richal.learnonline.service.Impl;

import com.richal.learnonline.doc.CourseDoc;
import com.richal.learnonline.repository.CourseRepository;
import com.richal.learnonline.service.ESCourseSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ESCourseSaveServiceImpl implements ESCourseSaveService {


    @Autowired
    private CourseRepository courseRepository;

    @Override
    public void save(List<CourseDoc> courseDocs) {
        System.out.println("ESCourseSaveServiceImpl.save方法被调用，文档数量: " + (courseDocs != null ? courseDocs.size() : 0));
        
        if(CollectionUtils.isEmpty(courseDocs)) {
            System.out.println("没有课程文档需要保存");
            return;
        }
        
        try {
            System.out.println("开始保存课程文档到ES...");
            courseRepository.saveAll(courseDocs);
            System.out.println("课程文档保存成功！");
        } catch (Exception e) {
            System.err.println("保存课程文档到ES时发生异常: " + e.getMessage());
            e.printStackTrace();
            throw e; // 重新抛出异常，让上层处理
        }
    }
}
