package kr.hhplus.be.server.interfaces.response;

import kr.hhplus.be.server.application.out.UserWalletInfo;

import java.math.BigDecimal;

public record UserWalletResponse(
        long userId,
        BigDecimal currentAmount
) {

    public static UserWalletResponse from(UserWalletInfo userWalletInfo) {
        return new UserWalletResponse(
                userWalletInfo.userId(),
                userWalletInfo.currentAmount()
        );
    }
}
