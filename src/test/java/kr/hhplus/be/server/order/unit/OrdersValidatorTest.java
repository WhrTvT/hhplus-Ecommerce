package kr.hhplus.be.server.order.unit;

import kr.hhplus.be.server.interfaces.support.exception.CustomException;
import kr.hhplus.be.server.domain.order.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OrdersValidatorTest {

    @Mock
    OrdersRepository ordersRepository;

    @Mock
    OrderDetailRepository orderDetailRepository;

    @InjectMocks
    OrderValidator orderValidator;

    @Test
    @DisplayName("🔴 주문이 존재하지 않으면 BusinessException 발생")
    void testOrderNotFound() {
        // given
        Mockito.when(ordersRepository.findByIdWithLock(1L)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> {
            orderValidator.validateOfOrderFindById(1L);
        }).isInstanceOf(CustomException.class).hasMessage("Order Not Found");
    }

    @Test
    @DisplayName("🟢 주문이 존재하면 Order 정보를 리턴")
    void testOrderFound() {
        // given
        Orders order = Orders.builder()
                .orderId(1L)
                .userId(1L)
                .userCouponId(1L)
                .couponDiscount(new BigDecimal("10000"))
                .totalPrice(new BigDecimal("100000"))
                .finalPrice(new BigDecimal("90000"))
                .build();

        Mockito.when(ordersRepository.findByIdWithLock(1L)).thenReturn(Optional.of(order));

        // when
        Orders loadOrder = orderValidator.validateOfOrderFindById(1L);

        // then
        Assertions.assertThat(order).isEqualTo(loadOrder);
    }

    @Test
    @DisplayName("🔴 주문 상세가 존재하지 않으면 BusinessException 발생")
    void testOrderDetailNotFound() {
        // given
        List<OrderDetail> productIds = List.of();

        // when
        // then
        assertThatThrownBy(() -> {
            orderValidator.validateOfOrderDetailIsEmpty(productIds);
        }).isInstanceOf(CustomException.class).hasMessage("Order Detail Not Found");
    }

    @Test
    @DisplayName("🟢 주문 상세가 존재하면 OrderDetail 정보를 리턴")
    void testOrderDetailFound() {
        // given
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);

        OrderDetail orderDetail1 = OrderDetail.builder()
                .orderDetailId(1L)
                .userId(1L)
                .orderId(1L)
                .productId(1L)
                .selectQuantity(100L)
                .unitPrice(new BigDecimal("10000"))
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .orderDetailId(2L)
                .userId(1L)
                .orderId(1L)
                .productId(2L)
                .selectQuantity(10L)
                .unitPrice(new BigDecimal("1000"))
                .build();

        List<OrderDetail> mockOrderDetails = Arrays.asList(orderDetail1, orderDetail2);

        Mockito.when(orderDetailRepository.findAllByProductIds(productIds))
                .thenReturn(mockOrderDetails);

        // when
        List<OrderDetail> result = orderDetailRepository.findAllByProductIds(productIds);

        // then
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0).getProductId()).isEqualTo(1L);
        Assertions.assertThat(result.get(1).getProductId()).isEqualTo(2L);
    }
}
