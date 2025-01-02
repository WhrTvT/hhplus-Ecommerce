package kr.hhplus.be.server.interfaces.response;

public record PaymentResponse(
        long paymentId,
        long orderId,
        String method,
        String status,
        String paymentAt
) {
    public static PaymentResponse mock(long orderId) {
        return new PaymentResponse(
                1L,
                orderId,
                "CARD",
                "SUCCESS",
                "2024-01-03T04:30:01"
        );
    }
}
