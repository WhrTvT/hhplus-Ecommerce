package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository {
    Optional<Coupon> findById(long couponId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cq FROM CouponQuantity cq WHERE cq.couponId = :couponId")
    Optional<CouponQuantity> findCouponQuantityByIdWithLock(long couponId);

    Coupon save(Coupon coupon);

    CouponQuantity save(CouponQuantity couponQuantity);
}
