package kr.hhplus.be.server.interfaces.request;

import java.time.LocalDateTime;

public record PaymentRequest(
        long orderId,
        String method,
        LocalDateTime paymentAt
) {

}

