package kr.hhplus.be.server.interfaces.mock.request;

import java.time.LocalDateTime;

public record CouponIssueRequestMock(
        long couponId,
        long userId,
        LocalDateTime issueAt
) {
}
