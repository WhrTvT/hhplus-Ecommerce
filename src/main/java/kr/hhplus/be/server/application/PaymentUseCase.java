package kr.hhplus.be.server.application;

import kr.hhplus.be.server.application.in.PaymentCommand;
import kr.hhplus.be.server.application.out.PaymentInfo;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class PaymentUseCase {
    private final PaymentService paymentService;

    public PaymentInfo doPayment(long orderId, String method, LocalDateTime paymentAt) {
        Payment payment = paymentService.payment(PaymentCommand.of(orderId, method, paymentAt));

        return PaymentInfo.from(payment);
    }
}
