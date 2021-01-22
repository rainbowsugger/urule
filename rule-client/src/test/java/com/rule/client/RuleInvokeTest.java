package com.rule.client;

import com.rule.client.vo.InvokeRuleVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RuleInvokeTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void invokeRule() throws IOException {
        InvokeRuleVO invokeRuleVO = new InvokeRuleVO();
        Map<String, Map<String, Object>> entityParams = new HashMap<>();

        Map<String, Object> customer = new HashMap<>();
        customer.put("name", "Jacob");
        customer.put("nationality", 2);
        Map<String, Object> claimInfoList = new HashMap<>();
        List<Map<String, Object>> claimInfos = new ArrayList();
        Map<String, Object> claimInfo1 = new HashMap<>();
        claimInfo1.put("claimNo", "CLM000001");
        claimInfo1.put("claimStatus", "Accepted");
        Map<String, Object> claimInfo2 = new HashMap<>();
        claimInfo1.put("claimNo", "CLM000002");
        claimInfo1.put("claimStatus", "Cancelled");
        claimInfos.add(claimInfo1);
        claimInfos.add(claimInfo2);
        claimInfoList.put("claimInfos", claimInfos);
        entityParams.put("customer", customer);
        entityParams.put("claimInfoList", claimInfoList);

        Map<String, Object> objectParams = new HashMap<>();
        objectParams.put("DeathRiskSA", 100000);
        objectParams.put("NonMedicalSA", 90000);
        objectParams.put("CIRiskSA", 600000);

        invokeRuleVO.setEntityParams(entityParams);
        invokeRuleVO.setObjectParams(objectParams);
        List<String> flowIds = new ArrayList<>();
        flowIds.add("UW");
        invokeRuleVO.setFlowIds(flowIds);
        Map value = new HashMap();
        value.put("rule", "uwFlow");
        value.put("invokeRuleVO", invokeRuleVO);

        String uwDecision = testRestTemplate.postForObject("/test/uw/uwFlow", value, String.class);
        TestUtils.prettyPrintJson(uwDecision, "apply uw rule for package flow "+flowIds.get(0));
    }
}
