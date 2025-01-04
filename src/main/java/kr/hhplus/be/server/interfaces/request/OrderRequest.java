package kr.hhplus.be.server.interfaces.request;

import java.util.List;

public record OrderRequest(
    long userId,
    long user_coupon_id,
    List<OrderDetailList> orderDetailLists
) {
    public record OrderDetailList(long productId, long selectedQuantity) {

    }
}
