package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ProductImplRepository implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    @Transactional
    public Page<ProductWithProductStockDTO> findProductWithStock(Pageable pageable) {
        return productJpaRepository.findProductWithStock(pageable);
    }

    @Override
    public Product save(Product product) { return productJpaRepository.save(product); }
}
