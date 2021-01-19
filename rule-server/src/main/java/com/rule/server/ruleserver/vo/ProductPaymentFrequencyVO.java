package com.rule.server.ruleserver.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @Description 付款频率
* @Author  Jie.Lin
* @Date 2020-12-02 18:52:58
*/
@EqualsAndHashCode()
@Data
public class ProductPaymentFrequencyVO implements Serializable{

    private static final long serialVersionUID = -1761064238909048426L;

    private Long id;

    private Long productId;

    /*
    *   参考payment_frequency表
    */
    private Integer frequency;

    private BigDecimal modalFactor;





}