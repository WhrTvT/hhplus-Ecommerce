package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.BusinessLogicException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class CouponTest {

    @Mock
    CouponRepository couponRepository;

    @InjectMocks
    CouponValidator couponValidator;

    @Test
    @DisplayName("🔴 쿠폰이 존재하지 않으면 COUPON_NOT_FOUND 예외 발생")
    void testValidateOfCouponFindByIdThrowsException() {
        // given
        Mockito.when(couponRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponValidator.validateOfCouponFindById(1L))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessage(ExceptionCode.COUPON_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("🟢 쿠폰이 존재하면 정상 반환")
    void testValidateOfCouponFindByIdSuccess() {
        // given
        Coupon coupon = Coupon.builder().couponId(1L).build();
        Mockito.when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));

        // when
        Coupon result = couponValidator.validateOfCouponFindById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCouponId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("🔴 쿠폰 수량이 존재하지 않으면 QUANTITY_NOT_FOUND 예외 발생")
    void testValidateOfFindCouponQuantityByIdThrowsException() {
        // given
        Mockito.when(couponRepository.findCouponQuantityById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponValidator.validateOfFindCouponQuantityById(1L))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessage(ExceptionCode.QUANTITY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("🟢 쿠폰 수량이 존재하면 정상 반환")
    void testValidateOfFindCouponQuantityByIdSuccess() {
        // given
        CouponQuantity couponQuantity = new CouponQuantity(1L, 100);
        Mockito.when(couponRepository.findCouponQuantityById(1L)).thenReturn(Optional.of(couponQuantity));

        // when
        CouponQuantity result = couponValidator.validateOfFindCouponQuantityById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("🔴 쿠폰 수량이 0 이하일 때 COUPON_MAX_ISSUED 예외 발생")
    void testValidateOfQuantityIsZEROThrowsException() {
        // given
        long quantity = 0;

        // when & then
        assertThatThrownBy(() -> couponValidator.validateOfQuantityIsZERO(quantity))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessage(ExceptionCode.COUPON_MAX_ISSUED.getMessage());
    }

    @Test
    @DisplayName("🟢 쿠폰 수량이 0 이상이면 정상 통과")
    void testValidateOfQuantityIsZEROSuccess() {
        // given
        long quantity = 10;

        // when & then
        Assertions.assertThatNoException()
                .isThrownBy(() -> couponValidator.validateOfQuantityIsZERO(quantity));
    }
}