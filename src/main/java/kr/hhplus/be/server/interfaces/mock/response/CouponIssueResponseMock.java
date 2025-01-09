package kr.hhplus.be.server.interfaces.mock.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CouponIssueResponseMock(
    long userCouponId,
    long userId,
    long couponId,
    String status,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime issueAt
) {
    public static CouponIssueResponseMock mock(long couponId, long userId){
        return new CouponIssueResponseMock(
                1L,
                userId,
                couponId,
                "UNUSED",
                LocalDateTime.now()
        );
    }
}
