package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.user.UserWallet;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository {
    Optional<UserWallet> findUserWalletWithPaymentByOrderId(long orderId);

    Payment save(Payment payment);
}
