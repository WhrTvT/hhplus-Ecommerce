package kr.hhplus.be.server.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.interfaces.request.OrderRequest;
import kr.hhplus.be.server.interfaces.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order API", description = "주문을 관리합니다.")
@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
public class OrderController {
//    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);
//    private final OrderService orderService;

    // 상품 주문
    @Operation(summary = "상품 주문", description = "Body로 받은 주문 정보로 주문 내역을 생성합니다.")
    @Parameter(name = "orderRequest", description = "주문 생성 Req 정보")
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> doOrder(
            @RequestBody OrderRequest orderRequest
    ) {
        return ResponseEntity.ok(OrderResponse.mock(orderRequest.userId()));
    }
}
