package kr.hhplus.be.server.domain.coupon;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository {
    Optional<Coupon> findById(long couponId);

    Optional<CouponQuantity> findCouponQuantityById(long couponId);

    Coupon save(Coupon coupon);
}
