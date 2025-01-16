package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.application.in.UserWalletChargeCommand;
import kr.hhplus.be.server.application.in.UserWalletCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserValidator userValidator;

    public UserWallet charge(UserWalletChargeCommand userWalletChargeCommand) {
        userValidator.validateOfUserFindById(userWalletChargeCommand.userId());
        userValidator.validateOfAmountIsRefused(userWalletChargeCommand.chargeAmount());

        UserWallet wallet = userValidator.validateOfUserWalletFindByUserId(userWalletChargeCommand.userId());
        wallet.chargeAmount(userWalletChargeCommand.chargeAmount());

        return wallet;
    }

    public UserWallet get(UserWalletCommand userWalletCommand) {
        userValidator.validateOfUserFindById(userWalletCommand.userId());

        return userValidator.validateOfUserWalletFindByUserId(userWalletCommand.userId());
    }
}
