package kr.hhplus.be.server.application.out;

import kr.hhplus.be.server.domain.order.Orders;

import java.math.BigDecimal;

public record OrderInfo(
    long orderId,
    long userId,
    long userCouponId,
    BigDecimal couponDiscount,
    BigDecimal totalPrice,
    BigDecimal finalPrice
) {
    public static OrderInfo from(Orders orders) {
        return new OrderInfo(
                orders.getOrderId(),
                orders.getUserId(),
                orders.getUserCouponId(),
                orders.getCouponDiscount(),
                orders.getTotalPrice(),
                orders.getFinalPrice()
        );
    }
}
