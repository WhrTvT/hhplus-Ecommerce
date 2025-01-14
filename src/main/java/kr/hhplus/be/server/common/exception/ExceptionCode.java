package kr.hhplus.be.server.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    USER_NOT_FOUND(400, "User Not Found"),
    WALLET_NOT_FOUND(401, "Wallet Not Found"),
    COUPON_NOT_FOUND(402, "Coupon Not Found"),
    QUANTITY_NOT_FOUND(403, "Quantity Not Found"),
    COUPON_MAX_ISSUED(404, "Coupon Max Issued"),
    USER_COUPON_NOT_FOUND(405, "UserCoupon Not Found"),
    ORDER_NOT_FOUND(406, "Order Not Found"),
    WALLET_IS_DECLINED(407, "Wallet Is Declined"),
    ORDER_DETAIL_NOT_FOUND(408, "Order Detail Not Found"),
    PRODUCT_NOT_FOUND(409,"Product Not Found");

    private final int code;
    private final String message;
}