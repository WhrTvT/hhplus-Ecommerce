package kr.hhplus.be.server.interfaces.request;

public record CouponIssuedRequest(
        long couponId,
        long userId
) {
}
