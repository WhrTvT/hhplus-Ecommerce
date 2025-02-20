package kr.hhplus.be.server.application;

import kr.hhplus.be.server.application.in.CouponCommand;
import kr.hhplus.be.server.application.in.CouponIssueCommand;
import kr.hhplus.be.server.application.out.CouponInfo;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class CouponUseCase {
    private final CouponService couponService;

//    public CouponInfo couponIssue(long couponId, long userId, LocalDateTime issueAt) {
    public Boolean couponIssue(long couponId, long userId, LocalDateTime issueAt) {
        Coupon coupon = couponService.getCoupon(CouponCommand.of(couponId, userId));
        Boolean userCoupon = couponService.issue(CouponIssueCommand.of(coupon.getCouponId(), userId, issueAt));

//        return CouponInfo.from(userCoupon);
        return userCoupon;
    }

    public Page<CouponInfo> getUserCoupons(long userId, Pageable pageable) {
        Page<UserCoupon> userCoupons = couponService.getUserCoupons(userId, pageable);

        return userCoupons.map(CouponInfo::from);
    }
}
