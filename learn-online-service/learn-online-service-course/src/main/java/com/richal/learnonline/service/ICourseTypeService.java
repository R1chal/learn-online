package com.richal.learnonline.service;

import com.richal.learnonline.domain.CourseType;
import com.baomidou.mybatisplus.service.IService;
import com.richal.learnonline.vo.CourseTypeCrumbVO;
import com.richal.learnonline.vo.CourseTypeTreeVO;

import java.util.List;

/**
 * <p>
 * 课程目录 服务类
 * </p>
 *
 * @author Richal
 * @since 2025-07-22
 */
public interface ICourseTypeService extends IService<CourseType> {
    /**
     * 获取课程类型的树形结构数据，并包含每个类型下的课程数量
     *
     * @param pid 父ID，如果为null则获取所有
     * @param path 路径，如果提供则只获取该路径下的数据
     * @return 树形结构数据，包含课程数量
     */
    List<CourseTypeTreeVO> getTreeDataWithCourseCount(Long pid, String path);

    List<CourseTypeCrumbVO> crumbs(Long courseTypeId);

}
