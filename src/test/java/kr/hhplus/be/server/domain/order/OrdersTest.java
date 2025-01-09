package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.exception.BusinessLogicException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OrdersTest {

    @Mock
    OrdersRepository ordersRepository;

    @InjectMocks
    OrderValidator orderValidator;

    @Test
    @DisplayName("🔴 주문이 존재하지 않으면 BusinessException 발생")
    void testOrderNotFound() {
        // given
        Mockito.when(ordersRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> {
            orderValidator.validateOfOrderFindById(1L);
        }).isInstanceOf(BusinessLogicException.class).hasMessage("Order Not Found");
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

        Mockito.when(ordersRepository.findById(1L)).thenReturn(Optional.of(order));

        // when
        Orders loadOrder = orderValidator.validateOfOrderFindById(1L);

        // then
        Assertions.assertThat(order).isEqualTo(loadOrder);
    }
}
