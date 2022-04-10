package programmers.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import programmers.spring.order.OrderProperties;
import programmers.spring.voucher.FixedAmountVoucher;
import programmers.spring.voucher.JdbcVoucherRepository;
import programmers.spring.voucher.VoucherRepository;

import java.text.MessageFormat;
import java.util.UUID;

@SpringBootApplication
@ComponentScan(basePackages = {"programmers.spring.order", "programmers.spring.voucher", "programmers.spring.config"})
public class KdtApplication {
    public static void main(String[] args) {
        var springApplication = new SpringApplication(KdtApplication.class);
        springApplication.setAdditionalProfiles("local");
        var applicationContext = springApplication.run(args);

        var orderProperties = applicationContext.getBean(OrderProperties.class);
        System.out.println(MessageFormat.format("version -> {0}", orderProperties.getVersion()));
        System.out.println(MessageFormat.format("minimumOrderAmount -> {0}", orderProperties.getMinimumOrderAmount()));
        System.out.println(MessageFormat.format("supportVendors -> {0}", orderProperties.getSupportVendors()));
        System.out.println(MessageFormat.format("description -> {0}", orderProperties.getDescription()));

        var customerId = UUID.randomUUID();
        var voucherRepository = applicationContext.getBean(VoucherRepository.class);
        var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        System.out.println(MessageFormat.format("is Jdbc Repo - {0}", voucherRepository instanceof JdbcVoucherRepository));
        System.out.println(MessageFormat.format("is Jdbc Repo - {0}", voucherRepository.getClass().getCanonicalName()));
    }
}
