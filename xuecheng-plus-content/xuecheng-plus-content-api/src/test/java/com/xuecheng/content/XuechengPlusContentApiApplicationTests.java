package com.xuecheng.content;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseDao;
import com.xuecheng.content.mapper.CourseCategoryDao;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CourseCategoryService;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = XuechengPlusContentApiApplication.class)
class XuechengPlusContentApiApplicationTests {

    @Autowired
    CourseBaseDao courseBaseDao;
    @Autowired
    CourseCategoryDao courseCategoryDao;
    @Autowired
    CourseCategoryService courseCategoryService;
    @Autowired
    TeachplanMapper teachplanDao;


    /**
     * 分页查询
     */
    @Test
    void contextLoads() {
//        CourseBase courseBase = courseBaseDao.selectById(18);
//        Assertions.assertNotNull(courseBase);

        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto(null,"java",null);
        PageParams pageParams = new PageParams(1L, 2L);

        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()),CourseBase::getName,courseParamsDto.getCourseName());
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,courseParamsDto.getAuditStatus());

        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<CourseBase> pageResult = courseBaseDao.selectPage(page, queryWrapper);
        List<CourseBase> items = pageResult.getRecords();
        long total = pageResult.getTotal();

        PageResult<CourseBase> courseBasePageResult = new PageResult<>(items, total, pageParams.getPageNo(), pageParams.getPageSize());
        System.out.println("courseBasePageResult:"+courseBasePageResult);
    }

    /**
     * Mapper通用方法
     */
    @Test
    void baseMapperMethod(){
        List<CourseCategory> courseCategories = courseCategoryDao.selectList(new LambdaQueryWrapper<>());
        System.out.println(courseCategories);
    }

    /**
     * 课程类型树形结构递归
     */
    @Test
    void CategoryTreeService(){
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryService.queryTreeNodes("1");
        String jsonString = JSON.toJSONString(courseCategoryTreeDtos);
        System.out.println(jsonString);
    }

    @Test
    void test(){
        List<TeachplanDto> teachplanDtos = teachplanDao.selectTreeNodes(new Long(117));
        System.out.println(teachplanDtos);
    }

}
