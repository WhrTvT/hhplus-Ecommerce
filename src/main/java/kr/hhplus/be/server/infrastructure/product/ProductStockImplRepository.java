package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.product.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductStockImplRepository implements ProductStockRepository {
    private final ProductStockJpaRepository productStockJpaRepository;

    @Override
    public Optional<ProductStock> findByProductId(long productId) {
        return productStockJpaRepository.findByProductId(productId);
    }

    @Override
    public ProductStock save(ProductStock productStock) {
        return productStockJpaRepository.save(productStock);
    }
}
