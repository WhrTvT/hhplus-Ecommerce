package kr.hhplus.be.server.interfaces.response;

import kr.hhplus.be.server.application.out.ProductInfo;

import java.math.BigDecimal;

public record ProductResponse(
        long productId,
        String name,
        String detail,
        BigDecimal price,
        long quantity
) {

    public static ProductResponse from(ProductInfo productInfo){
        return new ProductResponse(
                productInfo.productId(),
                productInfo.name(),
                productInfo.detail(),
                productInfo.price(),
                productInfo.quantity()
        );
    }
}
