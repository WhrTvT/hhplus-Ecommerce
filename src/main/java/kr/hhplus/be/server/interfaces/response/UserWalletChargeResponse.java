package kr.hhplus.be.server.interfaces.response;

import kr.hhplus.be.server.application.out.UserWalletChargeInfo;

import java.math.BigDecimal;

public record UserWalletChargeResponse(
        long userId,
        BigDecimal currentAmount
) {

    public static UserWalletChargeResponse from(UserWalletChargeInfo userWalletChargeInfo) {
        return new UserWalletChargeResponse(
                userWalletChargeInfo.userId(),
                userWalletChargeInfo.currentAmount()
        );
    }
}
