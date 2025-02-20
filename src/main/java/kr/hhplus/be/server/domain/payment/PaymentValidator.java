package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import kr.hhplus.be.server.domain.user.UserWallet;
import kr.hhplus.be.server.domain.user.UserWalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentValidator {
    private final PaymentRepository paymentRepository;
    private final UserWalletRepository userWalletRepository;

    public UserWallet validateOfUserWalletFindByUserId(long userId) {
        return userWalletRepository.findByUserIdWithLock(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.PAYMENT_REQUIRED_SERVICE, LogLevel.WARN, "Wallet Is Declined"));
    }

    public Long validateOfAlready(long orderId) {
        if (paymentRepository.findPaymentByOrderIdAndStatusWithLock(orderId, "SUCCESS")) {
            throw new CustomException(ExceptionCode.CONFLICT_SERVICE, LogLevel.WARN, "This Order is Already been paid");
        } else {
            return orderId;
        }
    }
}
