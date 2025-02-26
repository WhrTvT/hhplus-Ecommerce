package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.interfaces.support.exception.CustomException;
import kr.hhplus.be.server.interfaces.support.exception.ExceptionCode;
import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    public Page<ProductWithProductStockDTO> validateOfProductFindById(Pageable pageable) {
        Page<ProductWithProductStockDTO> products = productRepository.findProductWithStockLock(pageable);

        if (products.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "Product not found");
        } else{
            return products;
        }
    }

    public ProductStock validateOfProductStockFindByProductId(long productId) {
        return productStockRepository.findByProductIdWithLock(productId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "Product Stock not found"));
    }

    public boolean validateOfProductStockQuantity(long selectQuantity, long getQuantity) {
        if (getQuantity < selectQuantity) {
//            throw new CustomException(ExceptionCode.CONFLICT_SERVICE, LogLevel.WARN, "Insufficient stock");
            return true;
        }

        return false;
    }
}