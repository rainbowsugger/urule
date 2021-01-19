package com.rule.server.ruleserver;

import com.bstek.urule.console.servlet.URuleServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author jie.lin
 */
@Component
public class URuleServletRegistration {
    @Bean
    public ServletRegistrationBean registerURuleServlet(){
        return new ServletRegistrationBean(new URuleServlet(),"/urule/*");
    }
}
