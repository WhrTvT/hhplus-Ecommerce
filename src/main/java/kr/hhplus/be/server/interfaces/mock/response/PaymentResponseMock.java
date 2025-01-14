package kr.hhplus.be.server.interfaces.mock.response;

public record PaymentResponseMock(
        long paymentId,
        long orderId,
        String method,
        String status,
        String paymentAt
) {
    public static PaymentResponseMock mock(long orderId) {
        return new PaymentResponseMock(
                1L,
                orderId,
                "CARD",
                "SUCCESS",
                "2024-01-03T04:30:01"
        );
    }
}
