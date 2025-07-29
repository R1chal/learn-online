package com.richal.learnonline.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.Course;
import com.richal.learnonline.domain.CourseType;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.CourseMapper;
import com.richal.learnonline.mapper.CourseTypeMapper;
import com.richal.learnonline.service.ICourseTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.richal.learnonline.vo.CourseTypeCrumbVO;
import com.richal.learnonline.vo.CourseTypeTreeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * <p>
 * 课程目录 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-22
 */
@Slf4j
@Service
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements ICourseTypeService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 查询树结构
     *
     * @param pid 父ID，如果为null则获取所有
     * @param path 路径，如果提供则只获取该路径下的数据
     * @return
     */
    @Override
    public List<CourseTypeTreeVO> getTreeDataWithCourseCount(Long pid, String path) {
        Object object = redisTemplate.opsForValue().get(BusinessConstants.REDIS_COURSE_TYPE_TREE_DATA);
        if(object != null) {
            log.info("从 redis 中获取数据:{}", object);
            return (List<CourseTypeTreeVO>) object;
        }
        List<CourseTypeTreeVO> courseTypeTreeVOS = getCourseTypeTreeVOS(pid, path);
        log.info("从数据库获取数据:{}", courseTypeTreeVOS);
        redisTemplate.opsForValue().set(BusinessConstants.REDIS_COURSE_TYPE_TREE_DATA, courseTypeTreeVOS);
        return courseTypeTreeVOS;
    }

    @Override
    public List<CourseTypeCrumbVO> crumbs(Long courseTypeId) {
        // 判断参数
        if(courseTypeId==null){
            throw new GlobleBusinessException("参数异常");

        }

        // 根据id查询
        CourseType courseType = selectById(courseTypeId);

        if(courseType==null){
            throw new GlobleBusinessException("参数异常");
        }

        // 获取path，
        String[] idStrs = courseType.getPath().split("\\.");

        List<CourseType> courseTypes = selectBatchIds(Arrays.asList(idStrs));

        List<CourseTypeCrumbVO> courseTypeCrumbsVos = new ArrayList<>();

        courseTypes.forEach(type -> {
            EntityWrapper<CourseType> wrapper = new EntityWrapper<>();

            wrapper.eq("pid",type.getPid());
            wrapper.notIn("id",type.getId());
            List<CourseType> others = selectList(wrapper);

            courseTypeCrumbsVos.add(new CourseTypeCrumbVO(type,others));
        });

        return courseTypeCrumbsVos;

    }

    private List<CourseTypeTreeVO> getCourseTypeTreeVOS(Long pid, String path) {
        // 构建查询条件
        EntityWrapper<CourseType> wrapper = new EntityWrapper<>();

        // 如果提供了path，则按path进行模糊查询
        if (!StringUtils.isEmpty(path)) {
            wrapper.like("path", path);
        }

        // 查询所有符合条件的课程类型
        List<CourseType> allTypes = this.selectList(wrapper);

        // 获取所有课程类型的ID
        List<Long> typeIds = new ArrayList<>();
        for (CourseType type : allTypes) {
            typeIds.add(type.getId());
        }

        // 查询每个课程类型下的课程数量
        Map<Long, Integer> courseCountMap = new HashMap<>();
        if (!typeIds.isEmpty()) {
            // 使用自定义的SQL查询直接获取每个类型的课程数量
            List<Map<String, Object>> countResults = courseMapper.getCourseCountsByTypeIds(typeIds);

            // 将查询结果转换为Map
            for (Map<String, Object> result : countResults) {
                Long typeId = ((Number) result.get("course_type_id")).longValue();
                Integer count = ((Number) result.get("count")).intValue();
                courseCountMap.put(typeId, count);
            }
        }

        // 将课程类型转换为树形结构，并设置课程数量
        List<CourseTypeTreeVO> result = buildTree(allTypes, pid);

        // 递归设置课程数量
        updateCourseCount(result, courseCountMap);

        return result;
    }

    /**
     * 递归更新树形结构中的课程数量
     *
     * @param nodes 节点列表
     * @param courseCountMap 课程数量映射
     */
    private void updateCourseCount(List<CourseTypeTreeVO> nodes, Map<Long, Integer> courseCountMap) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }

        for (CourseTypeTreeVO node : nodes) {
            // 设置当前节点的课程数量
            Integer count = courseCountMap.getOrDefault(node.getId(), 0);
            node.setTotalCount(count);

            // 递归处理子节点
            updateCourseCount(node.getChildren(), courseCountMap);

            // 累加子节点的课程数量
            for (CourseTypeTreeVO child : node.getChildren()) {
                node.setTotalCount(node.getTotalCount() + child.getTotalCount());
            }
        }
    }

    /**
     * 构建树形结构
     *
     * @param allTypes 所有课程类型
     * @param rootPid 根节点的父ID
     * @return 树形结构
     */
    private List<CourseTypeTreeVO> buildTree(List<CourseType> allTypes, Long rootPid) {
        List<CourseTypeTreeVO> result = new ArrayList<>();

        // 使用Map存储所有节点，方便查找
        Map<Long, CourseTypeTreeVO> nodeMap = new HashMap<>();

        // 先将所有节点转换为VO并放入Map
        for (CourseType type : allTypes) {
            CourseTypeTreeVO vo = CourseTypeTreeVO.fromCourseType(type);
            nodeMap.put(vo.getId(), vo);
        }

        // 构建树形结构
        for (CourseTypeTreeVO node : nodeMap.values()) {
            // 如果是根节点，直接添加到结果集
            if ((rootPid == null && node.getPid() == 0) ||
                (rootPid != null && rootPid.equals(node.getPid()))) {
                result.add(node);
            } else {
                // 非根节点，找到其父节点并添加到children列表
                CourseTypeTreeVO parent = nodeMap.get(node.getPid());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }

        return result;
    }
}
