package kr.hhplus.be.server.product.unit;

import kr.hhplus.be.server.domain.product.ProductStock;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductStockTest {

    @Test
    @DisplayName("ProductStock 객체 생성")
    void createProductStock() {
        //given
        long productId = 1;
        long quantity = 10;

        //when
        ProductStock productStock = Instancio.of(ProductStock.class)
                .set(Select.field("productId"), productId)
                .set(Select.field("quantity"), quantity)
                .create();

        // then
        assertThat(productStock.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("Product 객체의 quantity 변경")
    void updateProductStock() {
        //given
        long productId = 1;
        long beforeQuantity = 10;
        long afterQuantity = 5;

        //when
        ProductStock productStock = Instancio.of(ProductStock.class)
                .set(Select.field("productId"), productId)
                .set(Select.field("quantity"), beforeQuantity)
                .create();

        productStock.decrementalQuantity(afterQuantity);

        // then
        assertThat(productStock.getQuantity()).isEqualTo(afterQuantity);
    }
}
