package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

    Page<ProductWithProductStockDTO> findProductWithStockLock(Pageable pageable);

    Product save(Product product);
}
