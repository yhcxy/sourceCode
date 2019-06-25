package com.midea.isc.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class LovRedis {
    @ApiModelProperty(value = "唯一键")
    private Integer valueId;
    @ApiModelProperty(value = "类型id")
    private Integer typeId;
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    @ApiModelProperty(value = "父Id")
    private Integer parentId;
    @ApiModelProperty(value = "父值")
    private String parentValue;
    @ApiModelProperty(value = "显示值")
    private String label;
    @ApiModelProperty(value = "存值")
    private String value;
}
