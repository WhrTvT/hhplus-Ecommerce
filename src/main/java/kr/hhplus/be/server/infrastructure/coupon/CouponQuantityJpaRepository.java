package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.CouponQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponQuantityJpaRepository extends JpaRepository<CouponQuantity, Long> {
}
