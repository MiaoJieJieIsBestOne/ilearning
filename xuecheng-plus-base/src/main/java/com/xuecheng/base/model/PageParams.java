package com.xuecheng.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询-分页参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParams {
    /**
     * MybatisPlus的分页参数类型为Long，所以这里最好定义为Long
     */
    //当前页码
    @ApiModelProperty(value = "当前页码",example = "1")
    private Long pageNo = 1L;

    //每页记录数
    @ApiModelProperty(value = "每页记录数",example = "10")
    private Long pageSize = 10L;
}
