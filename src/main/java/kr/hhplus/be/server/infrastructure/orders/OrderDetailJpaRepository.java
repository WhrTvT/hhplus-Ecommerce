package kr.hhplus.be.server.infrastructure.orders;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailJpaRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT detail FROM OrderDetail detail WHERE detail.orderId = ?1")
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<OrderDetail> findAllByOrderId(long orderId);
}
