package programmers.spring.order;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

import java.text.MessageFormat;
import java.util.List;

@ComponentScan
public class OrderProperties implements InitializingBean {

    @Value("v1.1.1")
    private String version;

    @Value("0")
    private String minimumOrderAmount;

    @Value("D")
    private List<String> supportVendors;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(MessageFormat.format("[OrderProperties] version -> {0}", version));
        System.out.println(MessageFormat.format("[OrderProperties] minimumOrderAmount -> {0}", minimumOrderAmount));
        System.out.println(MessageFormat.format("[OrderProperties] supportVendors -> {0}", supportVendors));
    }

    public String getVersion() {
        return version;
    }

    public String getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public List<String> getSupportVendors() {
        return supportVendors;
    }
}
