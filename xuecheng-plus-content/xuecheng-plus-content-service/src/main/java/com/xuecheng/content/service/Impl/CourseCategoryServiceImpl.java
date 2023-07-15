package com.xuecheng.content.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.org.apache.xerces.internal.dom.PSVIElementNSImpl;
import com.xuecheng.content.mapper.CourseCategoryDao;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    CourseCategoryDao courseCategoryDao;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        List<CourseCategory> courseCategories = courseCategoryDao.selectList(new LambdaQueryWrapper<>());
        List<CourseCategoryTreeDto> list = courseCategories.stream()
                .filter(e -> e.getParentid().equals(id))
                .map(e -> {
                    CourseCategoryTreeDto courseCategoryTreeDto = new CourseCategoryTreeDto();
                    BeanUtils.copyProperties(e, courseCategoryTreeDto);
                    courseCategoryTreeDto.setChildrenTreeNodes(getChildren(e, courseCategories));
                    return courseCategoryTreeDto;
                })
                .collect(Collectors.toList());
        return list;
    }

    private List<CourseCategoryTreeDto> getChildren(CourseCategory courseCategory, List<CourseCategory> courseCategories) {
        List<CourseCategoryTreeDto> children = courseCategories.stream()
                .filter(e -> e.getParentid().equals(courseCategory.getId()))
                .map(e -> {
                    CourseCategoryTreeDto courseCategoryTreeDto = new CourseCategoryTreeDto();
                    BeanUtils.copyProperties(e, courseCategoryTreeDto);
                    courseCategoryTreeDto.setChildrenTreeNodes(getChildren(e, courseCategories));

                    //如下这段是为了解决前端代码的问题，一般不需要写
                    if (courseCategoryTreeDto.getChildrenTreeNodes() == null || courseCategoryTreeDto.getChildrenTreeNodes().size() == 0) {
                        courseCategoryTreeDto.setChildrenTreeNodes(null);
                    }

                    return courseCategoryTreeDto;
                })
                .collect(Collectors.toList());

        return children;
    }
}
