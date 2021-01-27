package com.rule.client;

import com.bstek.urule.Utils;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.service.KnowledgeService;
import com.rule.client.service.ProductBasicService;
import com.rule.client.service.RuleEntityUtil;
import com.rule.client.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class RuleController {


    private final ProductBasicService productBasicService;

    @Autowired
    public RuleController(ProductBasicService productBasicService){
        this.productBasicService = productBasicService;
    }

    @PostMapping("/uw/{rule}")
    public ResultResponse<List<String>> packageRule(@PathVariable(value = "rule") String rule,  @RequestBody InvokeRuleVO invokeRuleVO) throws IOException {
        //创建一个KnowledgeSession对象
        KnowledgeService knowledgeService =
            (KnowledgeService)Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
        KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("test_demo/"+rule);
        KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
        Map<String, Map<String, Object>> entityParams = invokeRuleVO.getEntityParams();
        Map<String, Object> objectParams = invokeRuleVO.getObjectParams();
        if (entityParams != null && !entityParams.isEmpty()) {
            entityParams.keySet().forEach(p -> {
                GeneralEntity entity = new GeneralEntity(p);
                Map<String, Object> entityParam = entityParams.get(p);
                RuleEntityUtil.convertEntity(entity, entityParam);
                session.insert(entity);
            });
        }
        if(invokeRuleVO.getFlowIds() != null && invokeRuleVO.getFlowIds().size() >0){
            invokeRuleVO.getFlowIds().forEach(p -> session.startProcess(p, objectParams));
        }else{
            List<GeneralEntity> paramList = new ArrayList<>();
            objectParams.keySet().forEach(key -> {
                if(objectParams.get(key) instanceof Collection){
                    List<Object> content = (List<Object>)objectParams.get(key);
                    if(content.size() > 0 && content.get(0) instanceof Map){
                        Map contentMap = (Map)content.get(0);
                        if(!contentMap.isEmpty()){
                            Object contentObject = contentMap.get(contentMap.keySet().iterator().next());
                            if(contentObject instanceof Map){
                                List<Map<String, Map<String, Object>>> object = (List<Map<String, Map<String, Object>>>)objectParams.get(key);
                                object.forEach(value -> {
                                    value.keySet().forEach( param -> {
                                        GeneralEntity entity = new GeneralEntity(param);
                                        //entity.put(param, value.get(param));
                                        RuleEntityUtil.convertEntity(entity, value.get(param));
                                        paramList.add(entity);
                                    });
                                });
                                objectParams.put(key, paramList);
                            }
                        }
                    }
                }
            });
            session.fireRules(objectParams);
        }
        List<String> uwResults = (List<String>)session.getParameter("uwDecisionList");

        return ResultResponse.success(uwResults);
    }

    @PostMapping("/uw/rule/{rule}")
    public ResultResponse<List<String>> packageRule2(@PathVariable(value = "rule") String rule, @RequestBody UwCheckVO uwCheckVO) throws IOException {
       // PolicyProductVO policyProductVO = new PolicyProductVO();
        //创建一个KnowledgeSession对象
        KnowledgeService knowledgeService =
            (KnowledgeService)Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
        KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("test_demo/"+rule);
        KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
        //customer
        GeneralEntity customer = new GeneralEntity("customer");
        List customerTags = new ArrayList<>();
        Map<String, Object> objectParams = new HashMap<>();
        if(uwCheckVO != null){
            if(uwCheckVO.getCustomerInfoVO() != null){
                CustomerInfoVO customerInfoVO = uwCheckVO.getCustomerInfoVO();
                Field[] fields = CustomerInfoVO.class.getDeclaredFields();
                Arrays.stream(fields).forEach(p -> {
                    p.setAccessible(true);
                    try {
                        Object field = p.get(customerInfoVO);
                        customer.put(p.getName(), field);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
                if(customerInfoVO.getCustomerTags() != null){
                    customerTags = customerInfoVO.getCustomerTags().stream().map(p -> {
                        Map customerTag = new HashMap();
                        customerTag.put("customerTag", p);
                        return customerTag;
                    }).collect(Collectors.toList());
                }
            }
            objectParams.put("customerTags", customerTags);
            //claim
            List<GeneralEntity> claimEntityList = new ArrayList();
            if(uwCheckVO.getClaimInfoList() != null){
                List<ClaimInfo> claimInfoList = uwCheckVO.getClaimInfoList();
                claimEntityList = claimInfoList.stream().map(p -> {
                    GeneralEntity claim = new GeneralEntity("claimInfo");
                    Field[] fields = ClaimInfo.class.getDeclaredFields();
                    Arrays.stream(fields).forEach(q -> {
                        q.setAccessible(true);
                        try {
                            Object field = q.get(p);
                            claim.put(q.getName(), field);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                    return claim;
                }).collect(Collectors.toList());
            }
            //policy
            if(uwCheckVO.getPolicyProductVO() != null){
                PolicyProductVO policyProductVO = uwCheckVO.getPolicyProductVO();
                BigDecimal deathRiskSA = productBasicService.calcRiskSA(policyProductVO.getProductId(), policyProductVO.getAmount(), RiskCategory.DEATH.getKey());
                BigDecimal ciRiskSA = productBasicService.calcRiskSA(policyProductVO.getProductId(), policyProductVO.getAmount(), RiskCategory.CRITICAL_ILLNESS.getKey());
                BigDecimal nonMedicalSA = productBasicService.getSaNonMedical(policyProductVO.getProductId());
                objectParams.put("deathRiskSA", deathRiskSA);
                objectParams.put("nonMedicalSA", nonMedicalSA);
                objectParams.put("ciRiskSA", ciRiskSA);
            }

            session.insert(customer);
            GeneralEntity claimInfoList = new GeneralEntity("claimInfoList");
            claimInfoList.put("claimInfos", claimEntityList);
            session.insert(claimInfoList);

            if(uwCheckVO.getFlowIds() != null && uwCheckVO.getFlowIds().size() > 0){
                uwCheckVO.getFlowIds().forEach(p -> session.startProcess(p, objectParams));
            }else{
                session.fireRules(objectParams);
            }
        }
        String uwResult = (String)session.getParameter("uwResult");
        List<String> uwResults = new ArrayList<>();
        if(session.getParameter("uwDecisionList") != null){
            uwResults = (List<String>)session.getParameter("uwDecisionList");
        }
        return ResultResponse.success(uwResults);
    }

}
