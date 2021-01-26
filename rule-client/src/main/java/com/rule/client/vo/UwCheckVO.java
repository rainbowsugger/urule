package com.rule.client.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UwCheckVO {

    private CustomerInfoVO customerInfoVO;

    private PolicyProductVO policyProductVO;

    private Map claimInfoList;

    List<String> flowIds;

}
