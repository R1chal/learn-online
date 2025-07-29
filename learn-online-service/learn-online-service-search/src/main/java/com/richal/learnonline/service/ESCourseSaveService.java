package com.richal.learnonline.service;

import com.richal.learnonline.doc.CourseDoc;
import com.richal.learnonline.dto.CourseSearchDTO;
import com.richal.learnonline.result.PageList;

import java.util.List;

public interface ESCourseSaveService {

    void save(List<CourseDoc> courseDocs);

    PageList<CourseDoc> search(CourseSearchDTO courseSearchDTO);
}
