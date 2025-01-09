package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponQuantity;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponImplRepository implements CouponRepository {
    private final CouponJpaRepository couponJpaRepository;


    @Override
    public Optional<Coupon> findById(long couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Optional<CouponQuantity> findCouponQuantityById(long couponId) {
        return couponJpaRepository.findCouponQuantityById(couponId);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }
}
