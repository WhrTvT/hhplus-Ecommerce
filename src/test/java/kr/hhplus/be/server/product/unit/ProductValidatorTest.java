package kr.hhplus.be.server.product.unit;

import kr.hhplus.be.server.interfaces.support.exception.CustomException;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductValidator;
import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductValidator productValidator;

    @Test
    @DisplayName("🔴 상품이 존재하지 않으면 BusinessException 발생")
    void testProductNotFound() {
        // given
        int page = 1;
        int size = 5;
        String sort = "name";

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sort));

        Mockito.when(productRepository.findProductWithStockLock(pageable)).thenReturn(Page.empty());

        // when
        // then
        assertThatThrownBy(() -> {
            productValidator.validateOfProductFindById(pageable);
        }).isInstanceOf(CustomException.class).hasMessage("Product Not Found");
    }

    @Test
    @DisplayName("🟢 상품이 존재하면 Products 정보를 리턴")
    void testProductFound() {
        // given
        int page = 1;
        int size = 5;
        String sort = "name";
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sort));

        List<ProductWithProductStockDTO> products = new ArrayList<>();
        products.add(new ProductWithProductStockDTO(1L, "1번 상품", "아 자고 싶다", new BigDecimal("100000"), 3L));
        products.add(new ProductWithProductStockDTO(2L, "2번 상품", "3주차는 쉬는 주차라고 했는데?", new BigDecimal("100"), 1_000L));
        products.add(new ProductWithProductStockDTO(3L, "3번 상품", "난 왜 밤을 새야되지?", new BigDecimal("999"), 10L));

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), products.size());
        List<ProductWithProductStockDTO> pageContent = products.subList(start, end);

        Page<ProductWithProductStockDTO> PageImpl = new PageImpl<>(pageContent, pageable, products.size());
        Mockito.when(productRepository.findProductWithStockLock(pageable)).thenReturn(PageImpl);

        // when
        Page<ProductWithProductStockDTO> loadProduct = productValidator.validateOfProductFindById(pageable);

        // then
        Assertions.assertThat(loadProduct.getContent()).isEqualTo(products);
    }
}
