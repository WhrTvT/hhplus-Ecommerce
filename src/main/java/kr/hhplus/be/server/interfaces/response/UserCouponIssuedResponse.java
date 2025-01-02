package kr.hhplus.be.server.interfaces.response;

public record UserCouponIssuedResponse(
    long userCouponId,
    long userId,
    long couponId,
    String issuedAt
) {
    public static UserCouponIssuedResponse mock(long couponId, long userId){
        return new UserCouponIssuedResponse(
                1L,
                userId,
                couponId,
                "2024-01-03T04:50:01"
        );
    }
}
