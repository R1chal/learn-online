package com.richal.learnonline.service.Impl;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.richal.learnonline.doc.CourseDoc;
import com.richal.learnonline.dto.CourseSearchDTO;
import com.richal.learnonline.repository.CourseRepository;
import com.richal.learnonline.result.PageList;
import com.richal.learnonline.service.ESCourseSaveService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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
            System.out.println("课程文档保存到 ES成功！");
        } catch (Exception e) {
            System.err.println("保存课程文档到ES时发生异常: " + e.getMessage());
            e.printStackTrace();
            throw e; // 重新抛出异常，让上层处理
        }
    }

    @Override
    public PageList<CourseDoc> search(CourseSearchDTO param){

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();


        if(StringUtils.isNotEmpty(param.getSortField())){
            String fieldName="price";
            switch(param.getSortField().toLowerCase()){
                case "xp" : fieldName = "onlineTime"; break;
                case "pl" : fieldName = "commentCount"; break;
                case "rq" : fieldName = "viewCount"; break;
                case "xl" : fieldName = "saleCount"; break;
            }
            SortOrder sortOrder = SortOrder.DESC;

            if(StringUtils.isNotEmpty(param.getSortType())&&
                    param.getSortType().equalsIgnoreCase("asc")){
                sortOrder = SortOrder.ASC;
            }


            // 指定查询条件
            builder.withSort(new FieldSortBuilder(fieldName).order(sortOrder));

        }


        // 分页
        builder.withPageable(PageRequest.of(param.getPage()-1,param.getRows()));

        // 条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();


        // 关键字
        if(StringUtils.isNotEmpty(param.getKeyword())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("name",param.getKeyword()));
        }



        // 分类，等级，价格等。。
        if(param.getCourseTypeId()!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("courseTypeId",
                    param.getCourseTypeId()));
        }

        if(StringUtils.isNotEmpty(param.getGradeName())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("gradeName",
                    param.getGradeName()));
        }

        if(null != param.getPriceMin()){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").
                    gte(param.getPriceMin()));
        }

        if(null != param.getPriceMax()){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").
                    gte(param.getPriceMax()));
        }



        builder.withQuery(boolQueryBuilder);



        Page<CourseDoc> page = courseRepository.search(builder.build());


        // 总条数，  每页数据
        return new PageList(page.getTotalElements(),page.getContent());

    }
}
