package com.rule.client.ruleclient.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LiTianLiang
 * @date  2020/12/26 0026 14:50
 * @version 1.0
 * @description ObjectFormula对象
 */
@Data
@ApiModel(value="ObjectFormula对象", description="产品公式表")
public class ObjectFormulaVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "对象类型 1:产品 2:责任 3:产品责任")
    @NotBlank(message = "对象类型不能为空")
    private String objectType;

    @ApiModelProperty(value = "对象类型的id 比如对象类型是产品就传产品的id ")
    @NotNull(message = "对象id不能为空")
    private Long objectId;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "公式id")
    private Long formulaId;
}
