package com.xuecheng.content.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseDao;
import com.xuecheng.content.mapper.CourseCategoryDao;
import com.xuecheng.content.mapper.CourseMarketDao;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    CourseBaseDao courseBaseDao;

    @Autowired
    CourseMarketDao courseMarketDao;

    @Autowired
    CourseCategoryDao courseCategoryDao;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {

        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()), CourseBase::getName, courseParamsDto.getCourseName())
                    .eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, courseParamsDto.getAuditStatus())
                    .eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus()), CourseBase::getStatus, courseParamsDto.getPublishStatus());

        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<CourseBase> pageResult = courseBaseDao.selectPage(page, queryWrapper);
        List<CourseBase> items = pageResult.getRecords();
        long total = pageResult.getTotal();

        PageResult<CourseBase> courseBasePageResult = new PageResult<>(items, total, pageParams.getPageNo(), pageParams.getPageSize());
        return courseBasePageResult;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {
        // 1. 合法性校验     此处注释，采用JSR303校验
//        if (StringUtils.isBlank(addCourseDto.getName())) XueChengPlusException.cast("课程名称为空");
//        if (StringUtils.isBlank(addCourseDto.getMt())) XueChengPlusException.cast("课程分类为空");
//        if (StringUtils.isBlank(addCourseDto.getSt())) XueChengPlusException.cast("课程分类为空");
//        if (StringUtils.isBlank(addCourseDto.getGrade())) XueChengPlusException.cast("课程等级为空");
//        if (StringUtils.isBlank(addCourseDto.getTeachmode())) XueChengPlusException.cast("教育模式为空");
//        if (StringUtils.isBlank(addCourseDto.getUsers())) XueChengPlusException.cast("适应人群为空");
//        if (StringUtils.isBlank(addCourseDto.getCharge())) XueChengPlusException.cast("收费规则为空");

        //course_base写入数据
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDto, courseBase);
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        courseBase.setAuditStatus("202002");
        courseBase.setStatus("203001");
        int insert = courseBaseDao.insert(courseBase);
        if (insert <= 0) XueChengPlusException.cast("添加课程失败");
        Long courseId = courseBase.getId();

        //course_market写入数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto, courseMarket);
        courseMarket.setId(courseId);
        saveCourseMarket(courseMarket);

        //返回信息
        return getCourseBaseInfo(courseId);
    }

    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        Long courseId = Long.valueOf(editCourseDto.getId());

        //course_base
        CourseBase courseBase = courseBaseDao.selectById(courseId);
        if (courseBase == null) XueChengPlusException.cast("课程不存在");

        if (!companyId.equals(courseBase.getCompanyId())) XueChengPlusException.cast("本机构只能修改本机构的课程");

        BeanUtils.copyProperties(editCourseDto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        int i = courseBaseDao.updateById(courseBase);
        if (i<=0) XueChengPlusException.cast("修改课程失败");

        //course_market
        CourseMarket courseMarket = courseMarketDao.selectById(courseId);
        if (courseMarket == null) XueChengPlusException.cast("课程营销信息不存在");

        BeanUtils.copyProperties(editCourseDto,courseMarket);
        courseMarketDao.updateById(courseMarket);

        return getCourseBaseInfo(courseId);
    }

    //单独写一个方法保存营销信息，存在则更新，不存在则添加
    private int saveCourseMarket(CourseMarket courseMarket) {
        //参数的合法性校验
        String charge = courseMarket.getCharge();
//        if (StringUtils.isEmpty(charge)) XueChengPlusException.cast("收费规则为空");
        if (charge.equals("201001") && (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0)){
            XueChengPlusException.cast("课程价格不能为空并且大于0");
        }

        Long id = courseMarket.getId();
        CourseMarket courseMarketExist = courseMarketDao.selectById(id);
        if (courseMarketExist == null){
            return courseMarketDao.insert(courseMarket);
        }else{
            return courseMarketDao.updateById(courseMarket);
        }

    }

    //根据ID从数据库查询课程信息
    @Override
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){
        CourseBase courseBase = courseBaseDao.selectById(courseId);
        if (courseBase == null) return null;

        CourseMarket courseMarket = courseMarketDao.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if (courseMarket != null) BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);

        CourseCategory courseCategoryMt = courseCategoryDao.selectById(courseBaseInfoDto.getMt());
        String lableMt = courseCategoryMt.getLabel();
        courseBaseInfoDto.setMtName(lableMt);

        CourseCategory courseCategorySt = courseCategoryDao.selectById(courseBaseInfoDto.getSt());
        String labelSt = courseCategorySt.getLabel();
        courseBaseInfoDto.setStName(labelSt);

        return courseBaseInfoDto;
    }



}
