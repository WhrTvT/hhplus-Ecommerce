package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    @Query("""
        SELECT uw
        FROM Payment p
        INNER JOIN Orders o ON p.orderId = o.orderId
        INNER JOIN UserWallet uw ON o.userId = uw.userId
        WHERE
            p.orderId = ?1
            AND o.finalPrice >= uw.currentAmount
    """)
    Optional<UserWallet> findUserWalletWithPaymentByOrderId(long orderId);

}
