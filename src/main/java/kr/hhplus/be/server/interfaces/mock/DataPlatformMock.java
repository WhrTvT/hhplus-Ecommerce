package kr.hhplus.be.server.interfaces.mock;

import kr.hhplus.be.server.interfaces.mock.response.PaymentResponseMock;

import java.time.LocalDateTime;

public record DataPlatformMock(
        long paymentId,
        long orderId,
        String method,
        String status,
        LocalDateTime paymentAt,
        String result
) {
    public static DataPlatformMock mock(long paymentId, long orderId, String method, String status, LocalDateTime paymentAt) {
        return new DataPlatformMock(
                paymentId,
                orderId,
                method,
                status,
                paymentAt,
                "데이터 플랫폼 전송 성공"
        );
    }
}
