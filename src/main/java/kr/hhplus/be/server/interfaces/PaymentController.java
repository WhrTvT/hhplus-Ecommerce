package kr.hhplus.be.server.interfaces;

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

@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
public class PaymentController {
//    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);
//    private final PaymentService paymentService;

    // 주문 결제
    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> doPayment(
            @RequestBody PaymentRequest paymentRequest
    ) {
        return ResponseEntity.ok(PaymentResponse.mock(paymentRequest.orderId()));
    }
}
