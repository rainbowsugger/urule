package com.rule.client.ruleclient;

import com.bstek.urule.Utils;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.service.KnowledgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class RuleController {
    @GetMapping("/uw")
    public String rule(@RequestParam Long data) throws IOException {
        //创建一个KnowledgeSession对象
        KnowledgeService knowledgeService =
            (KnowledgeService)Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
        KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("test_demo/testUW");
        KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);

        GeneralEntity customer = new GeneralEntity("com.bstek.entity.Customer");
        customer.put("nationality",data);
        session.insert(customer);
        session.fireRules();

        String uwResult = (String)session.getParameter("UWResult");
        return String.valueOf(uwResult);
    }
}
