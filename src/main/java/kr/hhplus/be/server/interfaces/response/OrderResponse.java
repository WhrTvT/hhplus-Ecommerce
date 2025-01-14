package kr.hhplus.be.server.interfaces.response;

import kr.hhplus.be.server.application.out.OrderInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record OrderResponse(
    long orderId,
    long userId,
    long userCouponId,
    BigDecimal couponDiscount,
    BigDecimal totalPrice,
    BigDecimal finalPrice
) {
    public static OrderResponse from(OrderInfo orderInfo) {
        return new OrderResponse(
                orderInfo.orderId(),
                orderInfo.userId(),
                orderInfo.userCouponId(),
                orderInfo.couponDiscount(),
                orderInfo.totalPrice(),
                orderInfo.finalPrice()
        );
    }
}
