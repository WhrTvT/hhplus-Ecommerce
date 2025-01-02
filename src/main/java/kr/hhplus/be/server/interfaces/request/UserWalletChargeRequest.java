package kr.hhplus.be.server.interfaces.request;

public record UserWalletChargeRequest(
        long userId,
        long chargeAmount
) {

}
