package kr.hhplus.be.server.application.in;

import kr.hhplus.be.server.domain.order.response.OrderDetailDTO;

import java.util.List;

public record OrdersCommand(
        long userId,
        long userCouponId,
        List<OrderDetailDTO> orderDetailLists
) {
    public static OrdersCommand of(long userId, long userCouponId, List<OrderDetailDTO> orderDetailLists) {
        return new OrdersCommand(
                userId,
                userCouponId,
                orderDetailLists
        );
    }
}