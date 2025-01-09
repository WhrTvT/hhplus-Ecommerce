package kr.hhplus.be.server.application.out;

import kr.hhplus.be.server.domain.user.UserWallet;

import java.math.BigDecimal;

public record UserWalletInfo(
        long userId,
        BigDecimal currentAmount
) {
    public static UserWalletInfo from(UserWallet userWallet) {
        return new UserWalletInfo(
                userWallet.getUserId(),
                userWallet.getCurrentAmount()
        );
    }
}
