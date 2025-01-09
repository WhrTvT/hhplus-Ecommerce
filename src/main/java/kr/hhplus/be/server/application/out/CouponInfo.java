package kr.hhplus.be.server.application.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.time.LocalDateTime;

public record CouponInfo(
        long userCouponId,
        long couponId,
        long userId,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime issueAt
) {
    public static CouponInfo from(UserCoupon userCoupon){
        return new CouponInfo(
                userCoupon.getUserCouponId(),
                userCoupon.getCouponId(),
                userCoupon.getUserId(),
                userCoupon.getStatus(),
                userCoupon.getIssueAt()
        );
    }
}
