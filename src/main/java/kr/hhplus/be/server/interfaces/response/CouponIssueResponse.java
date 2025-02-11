package kr.hhplus.be.server.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.application.out.CouponInfo;

import java.time.LocalDateTime;

public record CouponIssueResponse(
    long userCouponId,
    long userId,
    long couponId,
    String status,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime issueAt
) {

    public static CouponIssueResponse from(CouponInfo couponInfo){
        return new CouponIssueResponse(
                couponInfo.userCouponId(),
                couponInfo.userId(),
                couponInfo.couponId(),
                couponInfo.status(),
                couponInfo.issueAt()
        );
    }

    public static Boolean fromT(Boolean couponInfo){
        return true;
    }
}
