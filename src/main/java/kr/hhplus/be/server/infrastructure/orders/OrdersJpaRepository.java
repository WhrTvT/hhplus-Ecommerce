package kr.hhplus.be.server.infrastructure.orders;

import kr.hhplus.be.server.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersJpaRepository extends JpaRepository<Orders, Long> {

}
