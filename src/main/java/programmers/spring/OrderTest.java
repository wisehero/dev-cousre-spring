package programmers.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;
import programmers.spring.order.OrderItem;
import programmers.spring.service.OrderService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

public class OrderTest {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfiguration.class);

        var customerId = UUID.randomUUID();
        var orderService = ac.getBean(OrderService.class);
        var order = orderService.createOrder(customerId, new ArrayList<OrderItem>() {{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }});


        Assert.isTrue(order.totalAmount() == 100L, MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));
    }
}
