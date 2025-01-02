package kr.hhplus.be.server.interfaces;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.interfaces.request.OrderRequest;
import kr.hhplus.be.server.interfaces.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
public class OrderController {
//    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);
//    private final OrderService orderService;

    // 상품 주문
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> doOrder(
            @RequestBody OrderRequest orderRequest
    ) {
        return ResponseEntity.ok(OrderResponse.mock(orderRequest.userId()));
    }
}
