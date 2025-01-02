package kr.hhplus.be.server.interfaces.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public record ProductResponse(
        long productId,
        String name,
        String detail,
        long price,
        long quantity
) {

    public static Page<ProductResponse> mock(Pageable pageable){
        List<ProductResponse> products = new ArrayList<>();

        products.add(new ProductResponse(1L, "1번 상품", "아 자고 싶다", 100_000L, 3L));
        products.add(new ProductResponse(2L, "2번 상품", "3주차는 쉬는 주차라고 했는데?", 100L, 1_000L));
        products.add(new ProductResponse(3L, "3번 상품", "난 왜 밤을 새야되지?", 999L, 10L));

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), products.size());
        List<ProductResponse> pageContent = products.subList(start, end);

        return new PageImpl<>(pageContent, pageable, products.size());
    }
}
