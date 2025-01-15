package kr.hhplus.be.server.product.unit;

import kr.hhplus.be.server.domain.product.Product;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    @DisplayName("상품정보를 바탕으로 Product 객체 생성")
    void createProduct() {
        //given
        String productName = "상품1";
        String productDetail = "상품 디테일1";
        BigDecimal productPrice = new BigDecimal("10000");

        //when
        Product product = Instancio.of(Product.class)
                .set(Select.field("name"), productName)
                .set(Select.field("detail"), productDetail)
                .set(Select.field("price"), productPrice)
                .create();

        //then
        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getDetail()).isEqualTo(productDetail);
        assertThat(product.getPrice()).isEqualTo(productPrice);
    }
}
