package kr.hhplus.be.server.interfaces.response;

import java.util.ArrayList;
import java.util.List;

public record UserCouponResponse(
    long userId,
    List<UserCouPonListMock> couponListMocks
) {
    public static UserCouponResponse mock(long userId){

        List<UserCouPonListMock> userCouponLists = new ArrayList<>();

        userCouponLists.add(new UserCouPonListMock(1L, 1L, "2024-01-03T04:50:01", "USED", "10% 정률 쿠폰", 10L, true, 30, "2025-01-31T23:59:59"));
        userCouponLists.add(new UserCouPonListMock(2L, 2L, "2024-01-03T04:50:01", "AVAILABLE", "50,000원 정액 쿠폰", 50_000L, false, 30, "2025-02-15T23:59:59"));

        return new UserCouponResponse(
                userId,
                userCouponLists
        );
    }

    public record UserCouPonListMock(
            long userCouponId,
            long couponId,
            String issuedAt,
            String status,
            String couponName,
            long discount,
            boolean isPercent,
            long quantity,
            String expiredAt
    ) {
    }
}