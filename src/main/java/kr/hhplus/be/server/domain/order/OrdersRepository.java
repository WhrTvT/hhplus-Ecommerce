package kr.hhplus.be.server.domain.order;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository {
    Optional<Orders> findByIdWithLock(long orderId);

    Orders save(Orders orders);

}
