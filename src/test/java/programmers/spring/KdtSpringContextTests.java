package programmers.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import programmers.spring.order.OrderItem;
import programmers.spring.order.OrderService;
import programmers.spring.order.OrderStatus;
import programmers.spring.voucher.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
public class KdtSpringContextTests {

    @Configuration
    @ComponentScan(basePackages = {"programmers.spring.voucher", "programmers.spring.order"})
    static class Config {
//        @Bean
//        VoucherRepository voucherRepository() {
//            return new VoucherRepository() {
//                @Override
//                public Optional<Voucher> findById(UUID voucherId) {
//                    return Optional.empty();
//                }
//
//                @Override
//                public Voucher insert(Voucher voucher) {
//                    return null;
//                }
//            };
//        }
    }

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    OrderService orderService;

    @Autowired
    VoucherRepository voucherRepository;

    @Test
    @DisplayName("applicationContext가 생성 되어야한다.")
    void testApplicationContext() {
        assertThat(applicationContext, notNullValue());
    }

    @Test
    @DisplayName("VoucherRepository가 빈으로 등록되어 있어야 한다.")
    void testVoucherRepositoryCreation() {
        var bean = applicationContext.getBean(VoucherRepository.class);
        assertThat(bean, notNullValue());
    }


    @Test
    @DisplayName("orderService를 사용해서 주문을 생성할 수 있다.")
    void testOrderService() {
        // given
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);


        // when
        var order = orderService.createOrder(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 200, 1)), fixedAmountVoucher.getVoucherId());

        // then
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));
    }

}
