package com.richal.learnonline.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(indexName = "course", type = "_doc")
@AllArgsConstructor
@NoArgsConstructor
public class CourseDoc {

    // 文档id
    @Id
    private Long id;

    // 课程名称
    @Field(type = FieldType.Text)
    private String name;

    // 适用人群
    @Field(type = FieldType.Keyword)
    private String forUser;

    // 课程类型id
    @Field(type = FieldType.Long)
    private Long courseTypeId;

    // 课程等级
    @Field(type = FieldType.Keyword)
    private String gradeName;

    // 开课时间
    @Field(type = FieldType.Date)
    private Date startTime;

    // 结课时间
    @Field(type = FieldType.Date)
    private Date endTime;

    // 封面
    @Field(type = FieldType.Keyword)
    private String pic;

    // 课程讲师
    @Field(type = FieldType.Text)
    private String teacherNames;

    // 章节名称
    @Field(type = FieldType.Text)
    private String chapterName;

    // 收费: 免费2、精品字符串
    @Field(type = FieldType.Keyword)
    private Integer charge;

    // 售价
    private BigDecimal price;

    // 原价
    private BigDecimal priceOld;

    // 销量
    @Field(type = FieldType.Integer)
    private Integer saleCount;

    // 浏览量
    @Field(type = FieldType.Integer)
    private Integer viewCount;

    // 评论数
    @Field(type = FieldType.Integer)
    private Integer commentCount;
}
