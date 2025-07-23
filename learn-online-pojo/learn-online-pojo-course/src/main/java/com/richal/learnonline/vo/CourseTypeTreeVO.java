package com.richal.learnonline.vo;

import com.richal.learnonline.domain.CourseType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程类型树形结构VO
 *
 * @author Richal
 * @since 2025-07-22
 */
@Data
public class CourseTypeTreeVO {
    
    private Long id;
    private String name;
    private Long pid;
    private String logo;
    private String description;
    private Integer sortIndex;
    private String path;
    private Integer totalCount;
    private List<CourseTypeTreeVO> children = new ArrayList<>();
    
    /**
     * 将CourseType转换为CourseTypeTreeVO
     * @param courseType 课程类型
     * @return CourseTypeTreeVO
     */
    public static CourseTypeTreeVO fromCourseType(CourseType courseType) {
        CourseTypeTreeVO vo = new CourseTypeTreeVO();
        vo.setId(courseType.getId());
        vo.setName(courseType.getName());
        vo.setPid(courseType.getPid());
        vo.setLogo(courseType.getLogo());
        vo.setDescription(courseType.getDescription());
        vo.setSortIndex(courseType.getSortIndex());
        vo.setPath(courseType.getPath());
        vo.setTotalCount(courseType.getTotalCount());
        return vo;
    }
} 