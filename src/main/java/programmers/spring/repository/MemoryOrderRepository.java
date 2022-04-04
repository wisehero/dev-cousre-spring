package programmers.spring.repository;

import org.springframework.stereotype.Repository;
import programmers.spring.order.Order;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryOrderRepository implements OrderRepository {

    private final Map<UUID, Order> storage = new ConcurrentHashMap<>();

    @Override
    public void insert(Order order) {
        storage.put(order.getOrderId(), order);
    }
}
