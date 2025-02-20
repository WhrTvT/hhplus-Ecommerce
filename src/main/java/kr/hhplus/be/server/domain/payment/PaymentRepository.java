package kr.hhplus.be.server.domain.payment;

import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {
    boolean findPaymentByOrderIdAndStatusWithLock(long orderId, String status);

    Payment save(Payment payment);
}
