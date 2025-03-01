package kr.hhplus.be.server.interfaces.mock.response;

import java.util.ArrayList;
import java.util.List;

public record TopProductResponseMock(
        int top,
        int day,
        List<Product> products
) {

    public static TopProductResponseMock mock(int top, int day){
        List<Product> products = new ArrayList<>();

        products.add(new Product(1L, "1번 상품", 100_000L, 3L));
        products.add(new Product(2L, "2번 상품", 100L, 1_000L));
        products.add(new Product(3L, "3번 상품", 999L, 10L));

        return new TopProductResponseMock(
                top,
                day,
                products
        );
    }

    public record Product(
        long productId,
        String productName,
        long price,
        long quantity
    ) {
    }
}
