package kr.hhplus.be.server.domain.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponRepository {

    Optional<UserCoupon> findById(long userCouponId);

    Page<UserCoupon> findByUserId(long userId, Pageable pageable);

    UserCoupon save(UserCoupon userCoupon);
}
