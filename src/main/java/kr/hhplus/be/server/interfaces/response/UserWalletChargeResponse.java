package kr.hhplus.be.server.interfaces.response;

public record UserWalletChargeResponse(
        long userId,
        long currentAmount
) {
    public static UserWalletChargeResponse mock(long userId){
        return new UserWalletChargeResponse(
                userId,
                100_000L
        );
    }
}
