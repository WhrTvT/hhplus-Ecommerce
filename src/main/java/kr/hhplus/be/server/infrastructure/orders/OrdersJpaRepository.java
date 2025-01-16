package kr.hhplus.be.server.infrastructure.orders;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersJpaRepository extends JpaRepository<Orders, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Orders o WHERE o.orderId = ?1")
    Optional<Orders> findByIdWithLock(Long orderId);
}
