package com.rule.client;

import com.bstek.urule.Utils;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.service.KnowledgeService;
import com.rule.client.service.RuleEntityUtil;
import com.rule.client.vo.InvokeRuleVO;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class RuleController {
    @PostMapping("/uw/{rule}")
    public String packageRule(@PathVariable(value = "rule") String rule,  @RequestBody InvokeRuleVO invokeRuleVO) throws IOException {
        //创建一个KnowledgeSession对象
        KnowledgeService knowledgeService =
            (KnowledgeService)Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
        KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("test_demo/"+rule);
        KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
        Map<String, Map<String, Object>> entityParams = invokeRuleVO.getEntityParams();
        Map<String, Object> objectParams = invokeRuleVO.getObjectParams();
        if (!entityParams.isEmpty()) {
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
            session.fireRules(objectParams);
        }
        String uwResult = (String)session.getParameter("UWResult");
        List<String> uwResults = (List<String>)session.getParameter("uwDecisionList");

        return uwResults.toString();
    }

}
