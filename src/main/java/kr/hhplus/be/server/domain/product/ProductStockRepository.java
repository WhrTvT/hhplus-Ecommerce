package kr.hhplus.be.server.domain.product;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStockRepository {
    Optional<ProductStock> findByProductId(long productId);

    ProductStock save(ProductStock productStock);
}
