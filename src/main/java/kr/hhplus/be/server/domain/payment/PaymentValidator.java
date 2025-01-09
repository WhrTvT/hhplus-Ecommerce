package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.common.exception.BusinessLogicException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import kr.hhplus.be.server.domain.user.UserWallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentValidator {
    private final PaymentRepository paymentRepository;

    public UserWallet validateOfFindUserWalletWithPaymentByOrderId(long orderId) {
        return paymentRepository.findUserWalletWithPaymentByOrderId(orderId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.WALLET_IS_DECLINED));
    }
}
