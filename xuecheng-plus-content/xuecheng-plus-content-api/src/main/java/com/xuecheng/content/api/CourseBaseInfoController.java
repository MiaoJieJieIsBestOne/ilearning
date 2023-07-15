package com.xuecheng.content.api;

import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController     //相当于@Controller和@ResponseBody
@Api(value = "课程信息管理接口",tags = "课程信息管理接口")
public class CourseBaseInfoController {

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    /**
     * @RequestBody注解的作用：将请求的JSON字符串转换为对象
     * @param pageParams
     * @param queryCourseParamsDto
     * @return
     */
    @PostMapping("/course/list")
    @ApiOperation("课程分页查询接口")
    public PageResult<CourseBase> list(PageParams pageParams,@RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto){
        return courseBaseInfoService.queryCourseBaseList(pageParams,queryCourseParamsDto);
    }

    @PostMapping("/course")
    @ApiOperation("新增课程")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Insert.class) AddCourseDto addCourseDto){
        //TODO companyId的值与单点登录相关，先硬编码
        Long companyId = 1232134443L;

        //int i = 1/0;      //系统异常测试

        return courseBaseInfoService.createCourseBase(companyId,addCourseDto);
    }

    @GetMapping("/course/{courseId}")
    @ApiOperation("根据课程ID查询课程信息")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId){
        return courseBaseInfoService.getCourseBaseInfo(courseId);
    }

    @PutMapping("/course")
    @ApiOperation("修改课程")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated(ValidationGroups.Update.class) EditCourseDto editCourseDto){
        //TODO companyId的值与单点登录相关，先硬编码
        Long companyId = 1232134443L;

        return courseBaseInfoService.updateCourseBase(companyId,editCourseDto);
    }

}
