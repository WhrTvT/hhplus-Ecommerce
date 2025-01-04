package kr.hhplus.be.server.interfaces.request;

public record PaymentRequest(
        long orderId,
        String method
) {

}

