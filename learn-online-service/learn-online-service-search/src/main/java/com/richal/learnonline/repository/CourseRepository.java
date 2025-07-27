package com.richal.learnonline.repository;

import com.richal.learnonline.doc.CourseDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends ElasticsearchRepository<CourseDoc, Long> {

}
