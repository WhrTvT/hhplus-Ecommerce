package kr.hhplus.be.server.domain.product.response;

import java.math.BigDecimal;

public record ProductWithProductStockDTO(
        long productId,
        String name,
        String detail,
        BigDecimal price,
        long quantity
) {
}
