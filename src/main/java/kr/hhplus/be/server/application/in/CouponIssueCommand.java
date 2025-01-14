package kr.hhplus.be.server.application.in;

import java.time.LocalDateTime;

public record CouponIssueCommand(
        long couponId,
        long userId,
        LocalDateTime issueAt
) {
    public static CouponIssueCommand of(long couponId, long userId, LocalDateTime issueAt) {
        return new CouponIssueCommand(
                couponId,
                userId,
                issueAt
        );
    }
}
