package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.user.UserWallet;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {
    UserWallet findUserWalletWithPaymentByOrderId(long orderId);

    boolean existsByOrderIdAndStatusWithLock(long orderId, String status);

    Payment findByOrderId(long orderId);

    Payment save(Payment payment);
}
