package com.rule.client.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class InvokeRuleVO {

    private Map<String, Map<String, Object>> entityParams;

    private Map<String, Object> objectParams;

    List<String> flowIds;

}
