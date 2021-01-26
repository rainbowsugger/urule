package com.rule.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rule.client.vo.CustomerInfoVO;
import com.rule.client.vo.InvokeRuleVO;
import com.rule.client.vo.PolicyProductVO;
import com.rule.client.vo.UwCheckVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RuleInvokeTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void invokeRule() throws Exception {
        InvokeRuleVO invokeRuleVO = new InvokeRuleVO();
        Map<String, Map<String, Object>> entityParams = new HashMap<>();

        Map<String, Object> customer = new HashMap<>();
        customer.put("name", "Jacob");
        customer.put("nationality", 2);
        customer.put("dsdsad","2222");

        Map<String, Object> claimInfoList = new HashMap<>();
        List<Map<String, Object>> claimInfos = new ArrayList();

        Map<String, Object> claimInfoParam1 = new HashMap<>();
        claimInfoParam1.put("claimNo", "CLM000001");
        claimInfoParam1.put("claimStatus", "Accepted");
        Map<String, Object> claimInfo1 = new HashMap<>();
        claimInfo1.put("claimInfo", claimInfoParam1);
        Map<String, Object> claimInfoParam2 = new HashMap<>();
        claimInfoParam2.put("claimNo", "CLM000002");
        claimInfoParam2.put("claimStatus", "Cancelled");
        Map<String, Object> claimInfo2 = new HashMap<>();
        claimInfo2.put("claimInfo", claimInfoParam2);

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
        /*MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.set("rule", "uwFlow");
        map.set("invokeRuleVO", invokeRuleVO);*/
        Map value = new HashMap();
        value.put("rule", "uwFlow");
        value.put("invokeRuleVO", invokeRuleVO);
        /*HttpHeaders headers = new HttpHeaders();
        //headers.setContentType();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        HttpEntity<String> strEntity = new HttpEntity<String>(JSONUtil.toJsonStr(value),headers);*/
        mockMvc.perform(
            //构造一个post请求
            MockMvcRequestBuilders.post("/test/uw/uwFlow")
                .contentType(MediaType.APPLICATION_JSON)
                //使用writeValueAsString()方法来获取对象的JSON字符串表示
                .content(mapper.writeValueAsString(invokeRuleVO)))
                //.content(JSONUtil.toJsonStr(value)))
            //andExpect，添加ResultMathcers验证规则，验证控制器执行完成后结果是否正确，【这是一个断言】
            .andExpect(MockMvcResultMatchers.status().is(200))

            //.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //假定返回的结果中，"name" 值为 "Mock测试2",如果不是的话，会抛出异常java.lang.AssertionError，并给出期望值和实际值
            //.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mock测试2"))

            //添加ResultHandler结果处理器，比如调试时 打印结果(print方法)到控制台
            //.andDo(print())

            //返回相应的MvcResult
            .andReturn();

        //tring uwDecision = testRestTemplate.postForObject("/test/uw/uwFlow", map, String.class);
        //TestUtils.prettyPrintJson(uwDecision, "apply uw rule for package flow "+flowIds.get(0));
    }

    @Test
    public void invokeRule2() throws Exception {
        CustomerInfoVO customerInfoVO = new CustomerInfoVO();
        customerInfoVO.setName("天地不仁以万物为刍狗");
        customerInfoVO.setNationality(1L);
        PolicyProductVO policyProductVO = new PolicyProductVO();
        policyProductVO.setProductId(85L);
        policyProductVO.setAmount(5222222L);
        List<String> flowIds = new ArrayList<>();
        flowIds.add("UW");

        Map<String, Object> claimInfoList = new HashMap<>();
        List<Map<String, Object>> claimInfos = new ArrayList();

        Map<String, Object> claimInfoParam1 = new HashMap<>();
        claimInfoParam1.put("claimNo", "CLM000001");
        claimInfoParam1.put("claimStatus", "Accepted");
        Map<String, Object> claimInfo1 = new HashMap<>();
        claimInfo1.put("claimInfo", claimInfoParam1);
        Map<String, Object> claimInfoParam2 = new HashMap<>();
        claimInfoParam2.put("claimNo", "CLM000002");
        claimInfoParam2.put("claimStatus", "Cancelled");
        Map<String, Object> claimInfo2 = new HashMap<>();
        claimInfo2.put("claimInfo", claimInfoParam2);

        claimInfos.add(claimInfo1);
        claimInfos.add(claimInfo2);

        claimInfoList.put("claimInfos", claimInfos);

        UwCheckVO uwCheckVO = new UwCheckVO();
        uwCheckVO.setCustomerInfoVO(customerInfoVO);
        uwCheckVO.setPolicyProductVO(policyProductVO);
        uwCheckVO.setClaimInfoList(claimInfoList);
        uwCheckVO.setFlowIds(flowIds);
        mockMvc.perform(
            //构造一个post请求
            MockMvcRequestBuilders.post("/test/uw/rule/uwFlow")
                .contentType(MediaType.APPLICATION_JSON)
                //.param("flowId", "UW")
                //使用writeValueAsString()方法来获取对象的JSON字符串表示
                .content(mapper.writeValueAsString(uwCheckVO)))
                .andDo(MockMvcResultHandlers.print())
                //content(mapper.writeValueAsString(param)))
                .andReturn();
    }
}
