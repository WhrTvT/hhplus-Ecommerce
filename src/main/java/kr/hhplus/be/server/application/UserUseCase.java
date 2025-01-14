package kr.hhplus.be.server.application;

import kr.hhplus.be.server.application.in.UserWalletChargeCommand;
import kr.hhplus.be.server.application.in.UserWalletCommand;
import kr.hhplus.be.server.application.out.UserWalletChargeInfo;
import kr.hhplus.be.server.application.out.UserWalletInfo;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.UserWallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class UserUseCase {
    private final UserService userService;

    public UserWalletChargeInfo userWalletCharge(long userId, BigDecimal chargeAmount){
        UserWallet userWallet = userService.charge(UserWalletChargeCommand.of(userId, chargeAmount));

        return UserWalletChargeInfo.from(userWallet);
    }

    public UserWalletInfo userWallet(long userId) {
        UserWallet userWallet = userService.get(UserWalletCommand.from(userId));

        return UserWalletInfo.from(userWallet);
    }
}
