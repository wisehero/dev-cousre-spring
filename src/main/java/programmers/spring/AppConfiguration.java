package programmers.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import programmers.spring.Order;
import programmers.spring.repository.OrderRepository;
import programmers.spring.repository.VoucherRepository;
import programmers.spring.service.OrderService;
import programmers.spring.service.VoucherService;
import programmers.spring.voucher.Voucher;

import java.util.Optional;
import java.util.UUID;

@Configuration
@ComponentScan
public class AppConfiguration {

    
}
