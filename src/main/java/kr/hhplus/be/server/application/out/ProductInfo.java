package kr.hhplus.be.server.application.out;

import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;

import java.math.BigDecimal;

public record ProductInfo(
        long productId,
        String name,
        String detail,
        BigDecimal price,
        long quantity
) {

    public static ProductInfo from(ProductWithProductStockDTO productWIthProductStockDTO){
        return new ProductInfo(
                productWIthProductStockDTO.productId(),
                productWIthProductStockDTO.name(),
                productWIthProductStockDTO.detail(),
                productWIthProductStockDTO.price(),
                productWIthProductStockDTO.quantity()
        );
    }
}
