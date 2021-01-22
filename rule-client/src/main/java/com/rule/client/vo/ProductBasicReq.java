package com.rule.client.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiTianLiang
 * @version 1.0
 * @date 2020/12/30 0030 15:04
 * @description ProductBasicReq
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("产品基础信息查询条件")
public class ProductBasicReq {

    @ApiModelProperty("当前页 从0开始")
    private Integer page;

    @ApiModelProperty("每页条数")
    private Integer size;

    @ApiModelProperty("产品主键")
    private Long productId;

    @ApiModelProperty("产品类型(0:都勾选 1:主险勾选，2:附加险勾选)")
    private Integer productType;

    @ApiModelProperty("产品编码名称")
    private String productCode;

    @ApiModelProperty("产品名称 中文英文都可以")
    private String productName;

    @ApiModelProperty("版本名称")
    private String versionName;

    @ApiModelProperty("产品状态(1:配置中，2:在售中，3:取消  4:下架)")
    private Integer status;

    @ApiModelProperty("产品分类(保险类别)")
    private Long categoryId;

    @ApiModelProperty("基础币种")
    private String baseCurrencyCode;

    public ProductBasicReq(String productCode){
        this.productCode = productCode;
    }
}
