package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.user.UserWallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class PaymentImplRepository implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;


    @Override
    public UserWallet findUserWalletWithPaymentByOrderId(long orderId) {
        return paymentJpaRepository.findUserWalletWithPaymentByOrderId(orderId);
    }

    @Override
    @Transactional
    public boolean existsByOrderIdAndStatusWithLock(long orderId, String status) {
        return paymentJpaRepository.existsByOrderIdAndStatusWithLock(orderId, status);
    }

    @Override
    public Payment findByOrderId(long orderId) {
        return paymentJpaRepository.findByOrderId(orderId);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
