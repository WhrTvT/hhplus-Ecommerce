package kr.hhplus.be.server.domain.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponRepository {

    Optional<UserCoupon> findById(long userCouponId);

    Page<UserCoupon> findAllByUserId(long userId, Pageable pageable);

    Optional<UserCoupon> findByUserIdAndCouponId(long userId, long couponId);

    UserCoupon save(UserCoupon userCoupon);
}
