package kr.hhplus.be.server.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.application.PaymentUseCase;
import kr.hhplus.be.server.application.out.PaymentInfo;
import kr.hhplus.be.server.interfaces.mock.DataPlatformMock;
import kr.hhplus.be.server.interfaces.mock.DataPlatformService;
import kr.hhplus.be.server.interfaces.request.PaymentRequest;
import kr.hhplus.be.server.interfaces.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment API", description = "결제를 관리합니다.")
@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentUseCase paymentUseCase;
    private final DataPlatformService dataPlatformService;

    // 주문 결제
    @Operation(summary = "주문 결제", description = "Body로 받은 결제 정보로 결제 내역을 생성합니다.")
    @Parameter(name = "paymentRequest", description = "결제 생성 Req 정보")
    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> doPayment(
            @Valid @RequestBody PaymentRequest paymentRequest
    ) {
        PaymentInfo paymentInfo = paymentUseCase.doPayment(paymentRequest.orderId(), paymentRequest.method(), paymentRequest.paymentAt());
        log.info("Payment Result : {}", paymentInfo);

//        var dataPlatform = DataPlatformMock.mock(paymentInfo.paymentId(), paymentInfo.orderId(), paymentInfo.method(), paymentInfo.status(), paymentInfo.paymentAt());
//        log.info("DataPlatform Result : {}", dataPlatform);

        // Mock API로 데이터 전송
        String dataPlatformResponse = dataPlatformService.sendPaymentToMockPlatform(paymentInfo);
        log.info("DataPlatform Mock API Response: {}", dataPlatformResponse);


//        return ResponseEntity.ok(PaymentResponseMock.mock(paymentRequest.orderId()));

        return ResponseEntity.ok(PaymentResponse.from(paymentInfo));
    }
}
