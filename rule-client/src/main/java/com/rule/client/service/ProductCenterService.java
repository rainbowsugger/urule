package com.rule.client.service;

import cn.hutool.json.JSONObject;
import com.rule.client.vo.ObjectFormulaVO;
import com.rule.client.vo.ProductBasicReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author jie.lin
 */
@FeignClient(url = "${product.url}",name = "productUrl")
public interface ProductCenterService {

    @RequestMapping(value = "product-center/formula/gets",method = RequestMethod.POST)
    JSONObject getProductFormula(@RequestBody ObjectFormulaVO model);

    @RequestMapping(value = "api/executor/run?id={fid}",method = RequestMethod.POST)
    JSONObject premiumCalculate(@RequestParam("id") long fid, @RequestBody Map<String, Object> inputs);

    @RequestMapping(value = "api/formulas/formula/{fid}",method = RequestMethod.GET)
    JSONObject getFormulaDetail(@PathVariable(value = "fid") long fid);

    @RequestMapping(value = "product-center/productBasic/findProductBasic",method = RequestMethod.POST)
    JSONObject getProductInfo(@RequestBody ProductBasicReq model);

    @RequestMapping(value = "product-center/productInfo/info",method = RequestMethod.GET)
    JSONObject getProductInfo(@RequestParam Long productId);

    @RequestMapping(value = "product-center/agreement/check/coolingOffSurrender",method = RequestMethod.POST)
    JSONObject checkCoolingOffRule(Map<String, Object> request);

    @RequestMapping(value = "notice/notice/email",method = RequestMethod.POST)
    JSONObject sendEmail(@RequestBody Map<String, String> emailInfo);

    @RequestMapping(value = "notice/notice/sms/notice",method = RequestMethod.POST)
    JSONObject sendSms(@RequestBody Map<String, String> smsInfo);

    @RequestMapping(value = "file-storage/file/upload",method = RequestMethod.POST)
    JSONObject upload(@RequestParam("file") MultipartFile file,
                      @RequestParam(value = "branchId", defaultValue = "1") long branchId);

}
