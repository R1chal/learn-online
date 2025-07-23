package com.richal.learnonline.mapper;

import com.richal.learnonline.domain.Course;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Richal
 * @since 2025-07-22
 */
public interface CourseMapper extends BaseMapper<Course> {
    
    /**
     * 获取指定课程类型ID列表中每个类型的课程数量
     * 
     * @param typeIds 课程类型ID列表
     * @return 课程类型ID和对应的课程数量的映射
     */
    @Select("<script>" +
            "SELECT course_type_id, COUNT(*) as count FROM t_course " +
            "WHERE course_type_id IN " +
            "<foreach item='item' collection='typeIds' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            " GROUP BY course_type_id" +
            "</script>")
    List<Map<String, Object>> getCourseCountsByTypeIds(@Param("typeIds") List<Long> typeIds);
}
