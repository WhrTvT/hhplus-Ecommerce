package kr.hhplus.be.server.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.application.out.CouponInfo;

import java.time.LocalDateTime;

public record CouponResponse(
        long userCouponId,
        long couponId,
        long userId,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime issueAt
) {
    public static CouponResponse from(CouponInfo couponInfo) {
        return new CouponResponse(
                couponInfo.userCouponId(),
                couponInfo.couponId(),
                couponInfo.couponId(),
                couponInfo.status(),
                couponInfo.issueAt()
        );
    }
}
