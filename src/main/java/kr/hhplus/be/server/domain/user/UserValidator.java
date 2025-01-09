package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.common.exception.BusinessLogicException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserWalletRepository userWalletRepository;
    private final UserRepository userRepository;

    public User validateOfUserFindById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    public UserWallet validateOfUserWalletFindByUserId(long userId) {
        return userWalletRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.WALLET_NOT_FOUND));
    }
}
