package kr.hhplus.be.server.interfaces.mock.request;

import kr.hhplus.be.server.domain.order.response.OrderDetailDTO;

import java.util.List;

public record OrderRequestMock(
        long userId,
        long user_coupon_id,
        List<OrderDetailDTO> orderDetailLists
) {
}
