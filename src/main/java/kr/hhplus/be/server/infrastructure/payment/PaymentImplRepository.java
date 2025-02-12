package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class PaymentImplRepository implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    @Transactional
    public boolean findPaymentByOrderIdAndStatusWithLock(long orderId, String status) {
        return paymentJpaRepository.findPaymentByOrderIdAndStatusWithLock(orderId, status);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
