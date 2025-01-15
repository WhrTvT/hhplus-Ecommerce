package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.logging.LogLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final ProductRepository productRepository;

    public Page<ProductWithProductStockDTO> validateOfProductFindById(Pageable pageable) {
        Page<ProductWithProductStockDTO> products = productRepository.findProductWithStock(pageable);

        if (products.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "Product not found");
        } else{
            return products;
        }
    }
}
