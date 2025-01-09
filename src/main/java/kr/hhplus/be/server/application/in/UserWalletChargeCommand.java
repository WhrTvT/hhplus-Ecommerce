package kr.hhplus.be.server.application.in;

import java.math.BigDecimal;

public record UserWalletChargeCommand(
        long userId,
        BigDecimal chargeAmount
) {
    public static UserWalletChargeCommand of(long userId, BigDecimal chargeAmount){
        return new UserWalletChargeCommand(
                userId,
                chargeAmount
        );
    }
}
