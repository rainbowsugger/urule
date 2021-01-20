package com.rule.client.ruleclient.service;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${policy.url}",name = "policyUrl")
public interface PolicyCenterService {

        @RequestMapping(value = "policy-center/product/getDetail",method = RequestMethod.GET)
        JSONObject getProduct(@RequestParam("productId") Integer productId);
}
