package kr.hhplus.be.server.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.OrderUseCase;
import kr.hhplus.be.server.application.out.OrderInfo;
import kr.hhplus.be.server.interfaces.support.log.ApiResponse;
import kr.hhplus.be.server.interfaces.request.OrderRequest;
import kr.hhplus.be.server.interfaces.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order API", description = "주문을 관리합니다.")
@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderUseCase orderUseCase;

    // 상품 주문
    @Operation(summary = "상품 주문", description = "Body로 받은 주문 정보로 주문 내역을 생성합니다.")
    @Parameter(name = "orderRequest", description = "주문 생성 Req 정보")
    @PostMapping("/order")
    public ApiResponse<OrderResponse> doOrder(
            @RequestBody OrderRequest orderRequest
    ) {
        OrderInfo orderInfo = orderUseCase.productOrder(orderRequest.userId(), orderRequest.user_coupon_id(), orderRequest.orderDetailLists());
        return ApiResponse.success(OrderResponse.from(orderInfo));
    }
}
