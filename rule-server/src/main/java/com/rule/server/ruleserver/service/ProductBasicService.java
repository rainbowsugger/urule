package com.rule.server.ruleserver.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.rule.server.ruleserver.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductBasicService {


    private final ProductCenterService productCenterService;

    private final PolicyCenterService policyCenterService;

    @Autowired
    public ProductBasicService(ProductCenterService productCenterService,
                               PolicyCenterService policyCenterService){
        this.productCenterService = productCenterService;
        this.policyCenterService = policyCenterService;
    }

    public Optional<Long> getProductFormulaDetail(Long objectId, String objectType, String businessType) throws Exception {
        ObjectFormulaVO queryModel = new ObjectFormulaVO();
        queryModel.setObjectId(objectId);
        queryModel.setObjectType(objectType);
        queryModel.setBusinessType(businessType);
        JSONObject formulaList = productCenterService.getProductFormula(queryModel);
        JSONArray results = JSONUtil.parseArray(formulaList.getJSONArray("data").toString());
        if(results.size() == 0){
            return Optional.empty();
        }
        ObjectFormulaVO formula = results.toList(ObjectFormulaVO.class).get(0);
        /*
        JSONObject formulaInfo = productCenterService.getFormulaDetail(formula.getFormulaId());
        Long.parseLong(JsonPath.read(JSONUtil.toJsonStr(formulaInfo),"$.data.id"));
        JSONObject formulaObject = formulaInfo.getJSONObject("data");
        FormulaDefine formulaDefine = JSONUtil.toBean(formulaObject, FormulaDefine.class);
         */
        return Optional.of(formula.getFormulaId());
    }

    public String getProductCode(Integer productId){
        JSONObject productJson = policyCenterService.getProduct(productId);
        String productCode = JsonPath.read(productJson.toString(), "$.data.productBasic.productCode");
        String versionName = JsonPath.read(productJson.toString(), "$.data.productBasic.versionName");
        return productCode + "_" + versionName;
    }

    public Map<Integer, BigDecimal> getPaymentFrequencyList(Integer productId) throws Exception {
        JSONObject productJson = policyCenterService.getProduct(productId);
        JSONArray frequencyJson = productJson.getJSONObject("data").getJSONArray("productPaymentFrequency");
        if(frequencyJson.size() == 0){
            throw  new Exception( "not found payument frequency for product id" + productId);
        }
        List<ProductPaymentFrequencyVO> frequencyVOS = frequencyJson.toList(ProductPaymentFrequencyVO.class);
        Map<Integer, BigDecimal> frequencyMap = frequencyVOS.stream().collect(Collectors.toMap(ProductPaymentFrequencyVO::getFrequency, ProductPaymentFrequencyVO::getModalFactor));
        return frequencyMap;
    }

    public Long getPremiumUnit(Integer productId){
        Configuration conf = Configuration.defaultConfiguration();
        JSONObject productJson = policyCenterService.getProduct(productId);
        Integer premiumUnit = JsonPath.using(conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)).parse(productJson.toString()).read("$.data.productPremiumCalculation.premiumUnit");
        if(premiumUnit == null){
            return 1L;
        }
        return premiumUnit.longValue();
    }

    public BigDecimal premiumCalculation(long formulaId, Map<String, Object> params){
        JSONObject premium = productCenterService.premiumCalculate(formulaId, params);
        return new BigDecimal(premium.getStr("data"));
    }

    public String calculation(long formulaId, Map<String, Object> params){
        JSONObject premium = productCenterService.premiumCalculate(formulaId, params);
        return premium.getStr("data");
    }

    public boolean checkCoolingOffRule(@NotNull Date applicationDate, @NotNull Date commencementDate, List<Long> productIds) throws Exception{
        if (Objects.isNull(commencementDate)) {
            throw new RuntimeException("CheckCoolingOffRule Missing CommencementDate");
        }
        if (Objects.isNull(productIds)) {
            throw new RuntimeException("CheckCoolingOffRule Missing ProductIds");
        }
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<Map<String, Long>> policyProducts =
                productIds.stream().map(id -> ImmutableMap.of("productId", id)).collect(Collectors.toList());
        Map<String, Object> request = ImmutableMap.of(
                "applicationDate", LocalDateTime.ofInstant(applicationDate.toInstant(), zone).format(df),
                "policy", ImmutableMap.of(
                        "commencementDate", LocalDateTime.ofInstant(commencementDate.toInstant(), zone).format(df),
                        "policyProducts", policyProducts)
        );
        String strResult = "false";
        try {
            strResult = productCenterService.checkCoolingOffRule(request).getStr("data");
        } catch (Exception e) {
            throw new RuntimeException("CheckCoolingOffRule Call Failed", e);
        }
        return "true".equalsIgnoreCase(strResult);
    }

    public Long sendEmail(Map<String, String> params) throws Exception {
        Map emailMap = new HashMap<>();
        try {
            JSONObject emailInfo = productCenterService.sendEmail(params);
            emailMap = JSONUtil.toBean(emailInfo.getJSONObject("data"),Map.class);
        }catch (Exception e){
            throw new RuntimeException("sendEmail call fail", e);
        }
        Integer id = (Integer) emailMap.get("notice id");
        return id.longValue();

    }

    public Long sendSms(Map<String, String> params) throws Exception {
        Map smsMap = new HashMap<>();
        try {
            JSONObject emailInfo = productCenterService.sendSms(params);
            smsMap = JSONUtil.toBean(emailInfo.getJSONObject("data"),Map.class);
        }catch (Exception e){
            throw new RuntimeException("sendSms call fail", e);
        }
        Integer id = (Integer) smsMap.get("notice id");
        return id.longValue();

    }

    @Transactional(rollbackFor = Exception.class)
    public List<StorageInfoVO> uploadFile(MultipartFile[] fileList, Long branchId) throws Exception {
        List<StorageInfoVO> uploadIds = new ArrayList<>();
        try {
            for(MultipartFile file : fileList){
                JSONObject fileInfo = productCenterService.upload(file, branchId);
                StorageInfoVO storageInfoVO = JSONUtil.toBean(fileInfo.getJSONObject("data"), StorageInfoVO.class);
                uploadIds.add(storageInfoVO);
            }
        }catch (Exception e){
            throw new Exception("upload file call fail", e);
        }
        return uploadIds;
    }

    public List<ProductBasicVO> findProductBasic(ProductBasicReq productBasicReq) throws Exception {
        List<ProductBasicVO> productBasicVOS = new ArrayList<>();
        try {
            JSONArray productList = productCenterService.getProductInfo(productBasicReq).getJSONObject("data").getJSONArray("content");
            productBasicVOS = productList.toList(ProductBasicVO.class);
        }catch (Exception e){
            throw new RuntimeException("find product basic call fail", e);
        }
        return productBasicVOS;
    }

    public static void main(String[] args) {

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Date applicationDate = new Date();
        Date commencementDate = new Date();
        List<Long> productIds = Collections.singletonList(1L);

        ZoneId zone = ZoneId.of("Asia/Shanghai");
        List<Map<String, Long>> policyProducts =
                productIds.stream().map(id -> ImmutableMap.of("productId", id)).collect(Collectors.toList());
        Map<String, Object> request = ImmutableMap.of(
                "applicationDate", LocalDateTime.ofInstant(applicationDate.toInstant(), zone).format(df),
                "policy", ImmutableMap.of(
                        "commencementDate", LocalDateTime.ofInstant(commencementDate.toInstant(), zone).format(df),
                        "policyProducts", policyProducts)
        );
    }

}
