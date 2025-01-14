package kr.hhplus.be.server.application.out;

import kr.hhplus.be.server.domain.user.UserWallet;

import java.math.BigDecimal;

public record UserWalletChargeInfo(
        long userId,
        BigDecimal currentAmount
) {
    public static UserWalletChargeInfo from(UserWallet userWallet) {
        return new UserWalletChargeInfo(
                userWallet.getUserId(),
                userWallet.getCurrentAmount()
        );
    }
}
