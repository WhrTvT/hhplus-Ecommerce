package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.exception.BusinessLogicException;
import kr.hhplus.be.server.domain.order.response.OrderDetailDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OrderDetailTest {

    @Mock
    OrderDetailRepository orderDetailRepository;

    @InjectMocks
    OrderValidator orderValidator;

    @Test
    @DisplayName("🔴 주문 상세가 존재하지 않으면 BusinessException 발생")
    void testOrderDetailNotFound() {
        // given
        List<OrderDetail> productIds = List.of();

        // when
        // then
        assertThatThrownBy(() -> {
            orderValidator.validateOfOrderDetailIsEmpty(productIds);
        }).isInstanceOf(BusinessLogicException.class).hasMessage("Order Detail Not Found");
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
                .select_quantity(100L)
                .unitPrice(new BigDecimal("10000"))
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .orderDetailId(2L)
                .userId(1L)
                .orderId(1L)
                .productId(2L)
                .select_quantity(10L)
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
