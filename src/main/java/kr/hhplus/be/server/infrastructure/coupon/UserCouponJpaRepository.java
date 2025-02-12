package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {
    Page<UserCoupon> findByUserId(long userId, Pageable pageable);

    @Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = ?1 AND uc.couponId = ?2")
    Optional<UserCoupon> findByUserIdAndCouponId(long userId, long couponId);
}
