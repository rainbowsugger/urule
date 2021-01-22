package com.rule.client.service;

import cn.hutool.core.util.StrUtil;
import com.bstek.urule.model.ExposeAction;
import org.springframework.stereotype.Component;

@Component(value = "stringUtil") public class StringUtil {
    @ExposeAction(value = "concat", parameters = {"string1", "string2"})
    public String concat(String str1, String str2) {
        return StrUtil.concat(true, str1, str2);
    }
}
