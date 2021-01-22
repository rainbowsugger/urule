package com.rule.client.vo;

import lombok.Data;

import java.io.Serializable;

/**
* @Description 
* @Author  WaltZ
* @Date 2020-12-21 15:23:02
*/
@Data
public class StorageInfoVO implements Serializable{

    private static final long serialVersionUID = 6302670720540816859L;

    private Long id;

    private String fileName;

    private Long length;

    private Long ownerId;

    private Long agencyId;
}