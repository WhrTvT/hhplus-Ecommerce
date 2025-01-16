package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import kr.hhplus.be.server.domain.user.UserWallet;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentValidator {
    private final PaymentRepository paymentRepository;

    public UserWallet validateOfFindUserWalletWithPaymentByOrderId(long orderId) {
        return paymentRepository.findUserWalletWithPaymentByOrderId(orderId)
                .orElseThrow(() -> new CustomException(ExceptionCode.PAYMENT_REQUIRED_SERVICE, LogLevel.WARN, "Wallet Is Declined"));
    }

    public Long validateOfAlready(long orderId) {
        if (paymentRepository.existsByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS)) {
            throw new CustomException(ExceptionCode.CONFLICT_SERVICE, LogLevel.WARN, "This Order is Already been paid");
        } else {
            return orderId;
        }
    }
}
