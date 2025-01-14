package kr.hhplus.be.server.application.in;

public record CouponCommand(
        long couponId,
        long userId
) {
    public static CouponCommand of(long couponId, long userId) {
        return new CouponCommand(
                couponId,
                userId
        );
    }
}
