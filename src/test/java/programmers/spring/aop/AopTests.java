package programmers.spring.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import programmers.spring.voucher.VoucherRepository;

@SpringJUnitConfig
public class AopTests {

    @Configuration
    @ComponentScan(
            basePackages = {"programmers.spring"}
    )
    @EnableAspectJAutoProxy
    static class Config {
    }

    @Autowired
    ApplicationContext context;

    @Autowired
    VoucherRepository voucherRepository;
     
}
