package kr.hhplus.be.server.application.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.domain.payment.Payment;

import java.time.LocalDateTime;

public record PaymentInfo(
        long paymentId,
        long orderId,
        String method,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime paymentAt
) {
    public static PaymentInfo from(Payment payment) {
        return new PaymentInfo(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getPaymentAt()
        );
    }
}
