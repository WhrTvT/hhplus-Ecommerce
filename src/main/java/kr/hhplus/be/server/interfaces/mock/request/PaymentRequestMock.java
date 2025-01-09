package kr.hhplus.be.server.interfaces.mock.request;

public record PaymentRequestMock(
        long orderId,
        String method
) {

}

