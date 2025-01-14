package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCustomRepository {
    List<ProductWithProductStockDTO> findProductWithStockByTop(long top, long day);
}
