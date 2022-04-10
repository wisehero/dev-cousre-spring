package programmers.spring.order;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "kdt")
public class OrderProperties implements InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(OrderProperties.class);

    private String version;

    private String minimumOrderAmount;

    private List<String> supportVendors;

    private String description;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMinimumOrderAmount(String minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public void setSupportVendors(List<String> supportVendors) {
        this.supportVendors = supportVendors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("[OrderProperties] version -> {0}", version);
        logger.debug("[OrderProperties] minimumOrderAmount -> {0}", minimumOrderAmount);
        logger.debug("[OrderProperties] supportVendors -> {0}", supportVendors);
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
