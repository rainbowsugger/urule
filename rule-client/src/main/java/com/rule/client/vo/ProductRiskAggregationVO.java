package com.rule.client.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @Description 产品风险累计
* @Author  Jie.Lin
* @Date 2020-12-02 18:52:58
*/
@Data
public class ProductRiskAggregationVO implements Serializable{

    private static final long serialVersionUID = -4855942121679747351L;

    private Long id;

    private Long productId;

    /*
    *   风险类型，对就risk_category表
    */
    private Integer riskCategory;

    private BigDecimal riskFactor;

    private Long sumRiskFormula;





}