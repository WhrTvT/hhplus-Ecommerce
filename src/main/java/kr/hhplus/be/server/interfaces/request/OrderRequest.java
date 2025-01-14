package kr.hhplus.be.server.interfaces.request;

import kr.hhplus.be.server.domain.order.response.OrderDetailDTO;

import java.util.List;

public record OrderRequest(
        long userId,
        long user_coupon_id,
        List<OrderDetailDTO> orderDetailLists
) {
}
