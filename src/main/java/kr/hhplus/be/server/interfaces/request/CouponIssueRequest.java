package kr.hhplus.be.server.interfaces.request;

import java.time.LocalDateTime;

public record CouponIssueRequest(
        long couponId,
//        long userId,
        LocalDateTime issueAt
) {
}
