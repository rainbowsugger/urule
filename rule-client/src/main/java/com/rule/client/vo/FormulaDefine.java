package com.rule.client.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FormulaDefine {

    private static final long serialVersionUID = 5460239771041325694L;

    private String code;

    private String category;

    private String name;

    private String description;


    private Long id;

    private Date createdAt;

    private Date updatedAt;

    private Long createdBy;

    private Long updatedBy;
}
