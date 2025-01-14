package kr.hhplus.be.server.infrastructure.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT cq FROM Coupon c INNER JOIN CouponQuantity cq ON c.couponId = cq.couponId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CouponQuantity> findCouponQuantityById(long couponId);
}
