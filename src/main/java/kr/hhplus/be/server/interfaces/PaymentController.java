package kr.hhplus.be.server.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.interfaces.request.PaymentRequest;
import kr.hhplus.be.server.interfaces.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment API", description = "결제를 관리합니다.")
@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
public class PaymentController {
//    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);
//    private final PaymentService paymentService;

    // 주문 결제
    @Operation(summary = "주문 결제", description = "Body로 받은 결제 정보로 결제 내역을 생성합니다.")
    @Parameter(name = "paymentRequest", description = "결제 생성 Req 정보")
    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> doPayment(
            @RequestBody PaymentRequest paymentRequest
    ) {
        return ResponseEntity.ok(PaymentResponse.mock(paymentRequest.orderId()));
    }
}
