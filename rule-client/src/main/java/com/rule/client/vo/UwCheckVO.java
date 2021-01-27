package com.rule.client.vo;

import lombok.Data;

import java.util.List;

@Data
public class UwCheckVO {

    private CustomerInfoVO customerInfoVO;

    private PolicyProductVO policyProductVO;

    private List<ClaimInfo> claimInfoList;

    List<String> flowIds;

}
