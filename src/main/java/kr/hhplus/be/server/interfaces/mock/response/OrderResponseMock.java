package kr.hhplus.be.server.interfaces.mock.response;

import java.util.ArrayList;
import java.util.List;

public record OrderResponseMock(
    long orderId,
    long userId,
    long paymentId,
    long userCouponId,
    long couponDiscount,
    long totalPrice,
    long finalPrice,
    List<OrderDetailListMock> orderDetailListMocks
) {
    public static OrderResponseMock mock(long userId){

        List<OrderDetailListMock> orderDetails = new ArrayList<>();

        orderDetails.add(new OrderDetailListMock(1L, 100_000L, 4L));
        orderDetails.add(new OrderDetailListMock(2L, 1_000L, 10L));
        orderDetails.add(new OrderDetailListMock(3L, 10_000L, 9L));

        return new OrderResponseMock(
                1L,
                userId,
                1L,
                1L,
                50_000L,
                500_000L,
                45_000L,
                orderDetails
        );
    }

    public record OrderDetailListMock(
            long orderDetailId,
            long unitPrice,
            long selectedQuantity
    ) {
    }
}
