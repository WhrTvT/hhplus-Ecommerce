package kr.hhplus.be.server.application;

import kr.hhplus.be.server.application.in.OrdersCommand;
import kr.hhplus.be.server.application.out.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.order.response.OrderDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderUseCase {
    private final OrderService orderService;

    public OrderInfo productOrder(long user_id, long user_coupon_id, List<OrderDetailDTO> orderDetailLists) {
        Orders order = orderService.order(OrdersCommand.of(user_id, user_coupon_id, orderDetailLists));

        return OrderInfo.from(order);
    }

}
