package com.xuecheng.content.api;


import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
public class TeachplanController {
    @Autowired
    TeachplanService teachplanService;

    @GetMapping("/teachplan/{courseId}/tree-nodes")
    @ApiOperation("查询课程计划树形结构")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){

        return teachplanService.findTeachplanTree(courseId);
    }

    @PostMapping("/teachplan")
    @ApiOperation("课程计划创建或修改")
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplan){
        teachplanService.saveTeachplan(teachplan);
    }

}
