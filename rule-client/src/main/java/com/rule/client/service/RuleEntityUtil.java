package com.rule.client.service;

import com.bstek.urule.model.GeneralEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleEntityUtil {
    public static void convertEntity(GeneralEntity entity, Map<String, Object> entityParam){
        //loop entity field
        entityParam.keySet().forEach(n -> {
            if(entityParam.get(n) instanceof List){
                //deal with list
                List<GeneralEntity> childList = ((List<Map<String ,Map<String, Object>>>)entityParam.get(n)).stream().map(m -> {
                    Map<String ,Map<String, Object>> childEntityParams = (Map<String, Map<String, Object>>)m;
                    //if(!childEntityParams.isEmpty()){
                    GeneralEntity childEntity;
                    String childKey = childEntityParams.keySet().stream().collect(Collectors.toList()).get(0);
                    childEntity = new GeneralEntity(childKey);
                    convertEntity(childEntity, childEntityParams.get(childKey));
                    return childEntity;
                    //}
                }).collect(Collectors.toList());
                entity.put(n, childList);
            }else {
                entity.put(n, entityParam.get(n));
            }
        });
    }
}
