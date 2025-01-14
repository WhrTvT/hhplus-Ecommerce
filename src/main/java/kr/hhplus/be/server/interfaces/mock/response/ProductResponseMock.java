package kr.hhplus.be.server.interfaces.mock.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record ProductResponseMock(
        long productId,
        String name,
        String detail,
        BigDecimal price,
        long quantity
) {

    public static Page<ProductResponseMock> mock(Pageable pageable){
        List<ProductResponseMock> products = new ArrayList<>();

        products.add(new ProductResponseMock(1L, "1번 상품", "아 자고 싶다", new BigDecimal("100000"), 3L));
        products.add(new ProductResponseMock(2L, "2번 상품", "3주차는 쉬는 주차라고 했는데?", new BigDecimal("100"), 1_000L));
        products.add(new ProductResponseMock(3L, "3번 상품", "난 왜 밤을 새야되지?", new BigDecimal("999"), 10L));

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), products.size());
        List<ProductResponseMock> pageContent = products.subList(start, end);

        return new PageImpl<>(pageContent, pageable, products.size());
    }
}
