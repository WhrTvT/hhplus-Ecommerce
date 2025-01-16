package kr.hhplus.be.server.infrastructure.orders;

import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.order.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrdersImplRepository implements OrdersRepository {
    private final OrdersJpaRepository ordersJpaRepository;

    @Override
    @Transactional
    public Optional<Orders> findByIdWithLock(long orderId) {
        return ordersJpaRepository.findByIdWithLock(orderId);
    }

    @Override
    public Orders save(Orders orders) { return ordersJpaRepository.save(orders); }
}
