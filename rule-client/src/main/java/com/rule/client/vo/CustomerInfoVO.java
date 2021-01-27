package com.rule.client.vo;

import lombok.Data;

import java.util.List;

@Data
public class CustomerInfoVO {

    private String name;

    private Long nationality;

    List<String> customerTags;
}
