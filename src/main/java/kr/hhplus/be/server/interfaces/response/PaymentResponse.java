package kr.hhplus.be.server.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.application.out.PaymentInfo;

import java.time.LocalDateTime;

public record PaymentResponse(
        long paymentId,
        long orderId,
        String method,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime paymentAt
) {
    public static PaymentResponse from(PaymentInfo paymentInfo) {
        return new PaymentResponse(
                paymentInfo.paymentId(),
                paymentInfo.orderId(),
                paymentInfo.method(),
                paymentInfo.status(),
                paymentInfo.paymentAt()
        );
    }
}
