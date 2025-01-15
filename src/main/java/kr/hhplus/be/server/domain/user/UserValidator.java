package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserWalletRepository userWalletRepository;
    private final UserRepository userRepository;

    public User validateOfUserFindById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "User not found"));
    }

    public UserWallet validateOfUserWalletFindByUserId(long userId) {
        return userWalletRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "Wallet not found"));
    }
}
