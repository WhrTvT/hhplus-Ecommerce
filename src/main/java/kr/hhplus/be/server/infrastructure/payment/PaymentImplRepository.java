package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.user.UserWallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PaymentImplRepository implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;


    @Override
    public Optional<UserWallet> findUserWalletWithPaymentByOrderId(long orderId) {
        return paymentJpaRepository.findUserWalletWithPaymentByOrderId(orderId);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
