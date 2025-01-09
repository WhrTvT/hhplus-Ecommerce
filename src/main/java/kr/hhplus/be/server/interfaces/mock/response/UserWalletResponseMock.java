package kr.hhplus.be.server.interfaces.mock.response;


import java.math.BigDecimal;

public record UserWalletResponseMock(
        long userId,
        BigDecimal currentAmount
) {

    public static UserWalletResponseMock mock(long userId){
        return new UserWalletResponseMock(
                userId,
                new BigDecimal("100,000")
        );
    }
}
