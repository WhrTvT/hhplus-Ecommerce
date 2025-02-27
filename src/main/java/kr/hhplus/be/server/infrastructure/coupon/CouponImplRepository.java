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
    private final CouponQuantityJpaRepository couponQuantityJpaRepository;


    @Override
    public Optional<Coupon> findById(long couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Optional<CouponQuantity> findCouponQuantityByIdWithLock(long couponId) {
        return couponQuantityJpaRepository.findCouponQuantityByIdWithLock(couponId);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public CouponQuantity save(CouponQuantity couponQuantity) {
        return couponQuantityJpaRepository.save(couponQuantity);
    }
}
