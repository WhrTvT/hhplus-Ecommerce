package kr.hhplus.be.server.application;

import kr.hhplus.be.server.application.out.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductUseCase {
    private final ProductService productService;

    public Page<ProductInfo> getProducts(Pageable pageable) {
        Page<ProductWithProductStockDTO> products = productService.findProducts(pageable);

        return products.map(ProductInfo::from);
    }

    public List<ProductInfo> getTopProducts(long top, long day){
        List<ProductWithProductStockDTO> topProducts = productService.findTopProducts(top, day);

        return topProducts.stream().map(ProductInfo::from).toList();
    }
}
