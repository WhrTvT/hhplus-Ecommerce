package kr.hhplus.be.server.domain.payment.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PaymentSuccessEvent {
    private final String paymentData;
    private final String outboxId;

    public static PaymentSuccessEvent create(String paymentData) {
        return new PaymentSuccessEvent(paymentData, UUID.randomUUID().toString());
    }
}