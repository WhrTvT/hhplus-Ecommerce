package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCustomRepository productCustomRepository;

    public Page<ProductWithProductStockDTO> findProducts(Pageable pageable) {
        return productRepository.findProductWithStock(pageable);
    }

    public List<ProductWithProductStockDTO> findTopProducts(long top, long day) {
        return productCustomRepository.findProductWithStockByTop(top, day);
    }
}
