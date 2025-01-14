package kr.hhplus.be.server.application.in;

import java.time.LocalDateTime;

public record PaymentCommand(
        long orderId,
        String method,
        LocalDateTime paymentAt
) {
    public static PaymentCommand of(long orderId, String method, LocalDateTime paymentAt) {
        return new PaymentCommand(
                orderId,
                method,
                paymentAt
        );
    }
}
