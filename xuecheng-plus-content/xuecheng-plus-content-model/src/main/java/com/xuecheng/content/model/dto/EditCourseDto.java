package com.xuecheng.content.model.dto;

import com.xuecheng.base.exception.ValidationGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "EditCourseDto", description = "更新课程基本信息")
public class EditCourseDto extends AddCourseDto{

    @NotEmpty(message = "修改课程ID不能为空", groups = {ValidationGroups.Update.class})
    @ApiModelProperty(value = "课程ID", required = true)
    private String id;

}
