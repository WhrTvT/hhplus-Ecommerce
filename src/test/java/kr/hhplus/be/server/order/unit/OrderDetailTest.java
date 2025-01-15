package kr.hhplus.be.server.order.unit;

import kr.hhplus.be.server.domain.order.OrderDetail;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderDetailTest {

    @Test
    @DisplayName("OrderDetail 객체 생성")
    void createOrderDetail(){
        //given
        long userId = 1;
        long orderId = 1;
        long productId = 1;
        long selectQuantity = 10;
        BigDecimal unitPrice = new BigDecimal("1000");

        //when
        OrderDetail orderDetail = Instancio.of(OrderDetail.class)
                .set(Select.field("userId"), userId)
                .set(Select.field("orderId"), orderId)
                .set(Select.field("productId"), productId)
                .set(Select.field("selectQuantity"), selectQuantity)
                .set(Select.field("unitPrice"), unitPrice)
                .create();

        //then
        assertThat(orderDetail.getUserId()).isEqualTo(userId);
        assertThat(orderDetail.getOrderId()).isEqualTo(orderId);
        assertThat(orderDetail.getProductId()).isEqualTo(productId);
        assertThat(orderDetail.getSelectQuantity()).isEqualTo(selectQuantity);
        assertThat(orderDetail.getUnitPrice()).isEqualTo(unitPrice);
    }
}
