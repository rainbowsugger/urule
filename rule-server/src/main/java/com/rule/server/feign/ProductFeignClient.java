package com.rule.server.feign;

import feign.HeaderMap;
import feign.QueryMap;
import feign.RequestLine;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Map;

@FeignClient(name = "ProductFeignClient")
public interface ProductFeignClient {

    @RequestLine("GET")
    Response get(URI baseUri, @HeaderMap Map<String, Object> headerMap, @QueryMap Map<String, Object> requestMap);

    @RequestLine("POST")
    Response post(URI baseUri, @HeaderMap Map<String, Object> headerMap, @RequestBody Map<String, Object> request);
}
