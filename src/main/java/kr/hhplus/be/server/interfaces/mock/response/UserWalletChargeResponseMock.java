package kr.hhplus.be.server.interfaces.mock.response;

import java.math.BigDecimal;

public record UserWalletChargeResponseMock(
        long userId,
        BigDecimal currentAmount
) {
    public static UserWalletChargeResponseMock mock(long userId){
        return new UserWalletChargeResponseMock(
                userId,
                new BigDecimal("100,000")
        );
    }
}
