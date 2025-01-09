package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCouponImplRepository implements UserCouponRepository {
    private final UserCouponJpaRepository userCouponJpaRepository;

    @Override
    public Optional<UserCoupon> findById(long userCouponId) {
        return userCouponJpaRepository.findById(userCouponId);
    }

    @Override
    public Page<UserCoupon> findByUserId(long userId, Pageable pageable) {
        return userCouponJpaRepository.findByUserId(userId, pageable);
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return userCouponJpaRepository.save(userCoupon);
    }
}
