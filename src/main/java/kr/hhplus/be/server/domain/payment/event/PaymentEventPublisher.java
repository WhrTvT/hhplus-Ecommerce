package kr.hhplus.be.server.domain.payment.event;

public interface PaymentEventPublisher {
    void paymentSuccess(PaymentSuccessEvent paymentSuccessEvent);
}
