package com.rule.server.ruleserver.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/*
 * @Description 产品基本信息
 * @Date 2020-12-02 18:52:58
 */
@EqualsAndHashCode()
@Data
@ApiModel("产品基础信息")
public class ProductBasicVO implements Serializable {

    private static final long serialVersionUID = -3827432530959926007L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("源产品主键")
    private Long sourceProductId;

    @ApiModelProperty("源产品编码")
    private String sourceProductCode;

    @ApiModelProperty("产品主键")
    private Long productId;

    @ApiModelProperty("产品类型(1:主险，2:附加险)")
    @NotNull(message = "产品类型不能为空")
    private Integer productType;

    @ApiModelProperty("产品期限类型(1:长期，2:短期)")
    @NotNull(message = "产品期限类型不能为空")
    private Integer termType;

    @ApiModelProperty("产品编码")
    @NotNull(message = "产品编码不能为空")
    private String productCode;

    @ApiModelProperty("开售日期")
    @NotNull(message = "开售日期不能为空")
    private Timestamp launchDate;

    @ApiModelProperty("英文名称")
    private String engName;

    @ApiModelProperty("英文名称简称")
    private String shortName;

    @ApiModelProperty("中文简体名称")
    private String chsName;

    @ApiModelProperty("中文简体名称简称")
    private String chsShortName;

    @ApiModelProperty("中文繁体名称")
    private String chtName;

    @ApiModelProperty("中文繁体简称")
    private String chtShortName;

    @ApiModelProperty("产品分类(保险类别)")
    private Long categoryId;

    @ApiModelProperty("产品状态(1:配置中，2:在售中，3:取消  4:下架)")
    private Integer status;

    @ApiModelProperty("是否豁免险(1:是，2:否)")
    private Integer isWaiver;

    @ApiModelProperty("是否有现价(1:是，2:否)")
    private Integer isCashValue;

    @ApiModelProperty("是否有分红(1:是，2:否)")
    private Integer isBonus;

    @ApiModelProperty("版本编码")
    private Integer versionCode;

    @ApiModelProperty("版本名称")
    private String versionName;

    @ApiModelProperty("下架日期")
    private java.util.Date shelveDate;

    @ApiModelProperty("是否高端医疗 0:否，1:是")
    private Integer vipMedical;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("更新人")
    private Long updateUser;
}