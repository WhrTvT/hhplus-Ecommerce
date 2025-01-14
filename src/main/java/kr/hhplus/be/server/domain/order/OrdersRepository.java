package kr.hhplus.be.server.domain.order;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository {
    Optional<Orders> findById(long orderId);

    Orders save(Orders orders);

}
