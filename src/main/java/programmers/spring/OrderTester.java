package programmers.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import programmers.spring.order.OrderItem;
import programmers.spring.order.OrderProperties;
import programmers.spring.order.OrderService;
import programmers.spring.voucher.FixedAmountVoucher;
import programmers.spring.voucher.JdbcVoucherRepository;
import programmers.spring.voucher.VoucherRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;


public class OrderTester {

    private static final Logger logger = LoggerFactory.getLogger(OrderTester.class);

    public static void main(String[] args) {

        var ac = new AnnotationConfigApplicationContext(AppConfiguration.class);


//        var environment = ac.getEnvironment();
//        environment.setActiveProfiles("local");
//        ac.refresh(); // 프로파일이 적용될 수 있게

//        var version = environment.getProperty("kdt.version");
//        var minimumOrderAmount = environment.getProperty("kdt.minimum-order-amount", Integer.class);
//        var supportVendors = environment.getProperty("kdt.support-vendors", List.class);
        //var description = environment.getProperty("kdt.description", List.class);
//        System.out.println(MessageFormat.format("version -> {0}", version));
//        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", minimumOrderAmount));
//        System.out.println(MessageFormat.format("supportVendors -> {0}", supportVendors));
//        System.out.println(MessageFormat.format("description -> {0}", description));

        var orderProperties = ac.getBean(OrderProperties.class);
        logger.info("logger name => {}", logger.getName());
        logger.info(MessageFormat.format("version -> {0}", orderProperties.getVersion()));
        logger.info(MessageFormat.format("minimumOrderAmount -> {0}", orderProperties.getMinimumOrderAmount()));
        logger.info(MessageFormat.format("supportVendors -> {0}", orderProperties.getSupportVendors()));
        logger.info(MessageFormat.format("description -> {0}", orderProperties.getDescription()));

//        var resource = ac.getResource("application.yaml");
//        System.out.println(MessageFormat.format("Resource -> {0}", resource.getClass().getCanonicalName()));


        var customerId = UUID.randomUUID();
        var voucherRepository = ac.getBean(VoucherRepository.class);
        var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        System.out.println(MessageFormat.format("is Jdbc Repo - {0}", voucherRepository instanceof JdbcVoucherRepository));
        System.out.println(MessageFormat.format("is Jdbc Repo - {0}", voucherRepository.getClass().getCanonicalName()));
        var orderService = ac.getBean(OrderService.class);
        var order = orderService.createOrder(customerId, new ArrayList<OrderItem>() {{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }}, voucher.getVoucherId());


        Assert.isTrue(order.totalAmount() == 90L, MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));

        ac.close();
    }
}
