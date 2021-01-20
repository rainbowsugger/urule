package com.rule.server.ruleserver;

import cn.hutool.core.util.StrUtil;
import com.bstek.urule.model.ExposeAction;
import org.springframework.stereotype.Component;

@Component(value = "StringUtil") public class StringUtil {
    @ExposeAction(value = "concat", parameters = {"string1", "string2", "string3"})
    public String concat(String str1, String str2, String str3) {
        return StrUtil.concat(true, str1, str2, str3);
    }
}
