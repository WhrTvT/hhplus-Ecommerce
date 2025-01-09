package kr.hhplus.be.server.infrastructure.orders;

import kr.hhplus.be.server.domain.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailJpaRepository extends JpaRepository<OrderDetail, Long> {
}
