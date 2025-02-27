package kr.hhplus.be.server.infrastructure.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.CouponQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponQuantityJpaRepository extends JpaRepository<CouponQuantity, Long> {
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cq FROM CouponQuantity cq WHERE cq.couponId = ?1")
    Optional<CouponQuantity> findCouponQuantityByIdWithLock(long couponId);
}
