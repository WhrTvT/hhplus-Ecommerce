package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.application.in.PaymentCommand;
import kr.hhplus.be.server.domain.order.OrderValidator;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.user.UserWallet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentValidator paymentValidator;
    private final OrderValidator orderValidator;

    @Transactional
    public Payment payment(PaymentCommand paymentCommand) {
        // 존재하는 주문인지
        Orders orders = orderValidator.validateOfOrderFindById(paymentCommand.orderId());

        // 잔액이 충분한지
        UserWallet userWallet = paymentValidator.validateOfFindUserWalletWithPaymentByOrderId(paymentCommand.orderId());

        // 잔액 차감
        userWallet.usedAmount(orders.getFinalPrice());

        // 결제 성공
        Payment savedPayment = paymentRepository.save(Payment.builder()
                .orderId(paymentCommand.orderId())
                .method(paymentCommand.method())
                .status(String.valueOf(PaymentStatus.SUCCESS))
                .paymentAt(paymentCommand.paymentAt())
                .build());

        // 통계로그 : 결제 시, 사용한 방법
        log.info("orderId: {}, method: {}, status: {}", orders.getOrderId(), paymentCommand.method(), savedPayment.getStatus());

        return savedPayment;
    }
}