package kr.hhplus.be.server.order.unit;

import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.Orders;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    private static final Logger log = LoggerFactory.getLogger(OrderTest.class);

    @Test
    @DisplayName("주문 정보를 바탕으로 Order 객체 생성")
    void createOrder(){
        //given
        long userId = 1;
        long userCouponId = 1;
        BigDecimal couponDiscount = new BigDecimal("10000");
        BigDecimal totalPrice = new BigDecimal("20000");
        BigDecimal finalPrice = new BigDecimal("10000");

        //when
        Orders order = Instancio.of(Orders.class)
                .set(Select.field("userId"), userId)
                .set(Select.field("userCouponId"), userCouponId)
                .set(Select.field("couponDiscount"), couponDiscount)
                .set(Select.field("totalPrice"), totalPrice)
                .set(Select.field("finalPrice"), finalPrice)
                .create();

        //then
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getUserCouponId()).isEqualTo(userCouponId);
        assertThat(order.getCouponDiscount()).isEqualTo(couponDiscount);
        assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(order.getFinalPrice()).isEqualTo(finalPrice);
    }

    @Test
    @DisplayName("Orders 객체의 couponDiscount, totalPrice, finalPrice 계산")
    void calculatePrice(){
        //given
        BigDecimal couponDiscount = new BigDecimal("10000");
        BigDecimal totalPrice = new BigDecimal("20000");
        BigDecimal finalPrice = new BigDecimal("10000");

        BigDecimal discount = new BigDecimal("2");
        boolean isPercent = true;

        OrderDetail orderDetail1 = OrderDetail.builder()
                .selectQuantity(1L)
                .unitPrice(new BigDecimal("10000"))
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .selectQuantity(10L)
                .unitPrice(new BigDecimal("1000"))
                .build();

        List<OrderDetail> orderDetails = Arrays.asList(orderDetail1, orderDetail2);

        //when
        Orders order = Instancio.of(Orders.class)
                .set(Select.field("couponDiscount"), couponDiscount)
                .set(Select.field("totalPrice"), totalPrice)
                .set(Select.field("finalPrice"), finalPrice)
                .create();

        BigDecimal calculateTotalPrice = order.getTotalPrice(orderDetails);
        BigDecimal calculateDiscount = order.getCouponDiscount(calculateTotalPrice, discount, isPercent);
        BigDecimal calculateFinalPrice = order.getFinalPrice(calculateTotalPrice, calculateDiscount);

        //then
        assertThat(order.getTotalPrice()).isEqualTo(calculateTotalPrice);
        assertThat(order.getFinalPrice()).isEqualTo(calculateFinalPrice);
    }
}
