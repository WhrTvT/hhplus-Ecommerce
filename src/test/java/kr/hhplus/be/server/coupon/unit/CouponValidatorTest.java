package kr.hhplus.be.server.coupon.unit;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import kr.hhplus.be.server.domain.coupon.*;
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
class CouponValidatorTest {

    @Mock
    CouponRepository couponRepository;

    @Mock
    UserCouponRepository userCouponRepository;

    @InjectMocks
    CouponValidator couponValidator;

    @Test
    @DisplayName("🔴 쿠폰이 존재하지 않으면 COUPON_NOT_FOUND 예외 발생")
    void testValidateOfCouponFindByIdThrowsException() {
        // given
        Mockito.when(couponRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponValidator.validateOfCouponFindById(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_SERVICE.getMessage());
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
        Mockito.when(couponRepository.findCouponQuantityByIdWithLock(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponValidator.validateOfFindCouponQuantityById(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_SERVICE.getMessage());
    }

    @Test
    @DisplayName("🟢 쿠폰 수량이 존재하면 정상 반환")
    void testValidateOfFindCouponQuantityByIdSuccess() {
        // given
        CouponQuantity couponQuantity = new CouponQuantity(1L, 100);
        Mockito.when(couponRepository.findCouponQuantityByIdWithLock(1L)).thenReturn(Optional.of(couponQuantity));

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
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.CONFLICT_SERVICE.getMessage());
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

    @Test
    @DisplayName("🔴 유저 쿠폰이 존재하지 않으면 COUPON_NOT_FOUND 예외 발생")
    void testValidateOfUserCouponFindByIdThrowsException() {
        // given
        Mockito.when(userCouponRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponValidator.validateOfUserCouponFindById(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_SERVICE.getMessage());
    }

    @Test
    @DisplayName("🟢 유저 쿠폰이 존재하면 정상 반환")
    void testValidateOfUserCouponFindByIdSuccess() {
        // given
        UserCoupon userCoupon = UserCoupon.builder().userCouponId(1L).build();
        Mockito.when(userCouponRepository.findById(1L)).thenReturn(Optional.of(userCoupon));

        // when
        UserCoupon result = couponValidator.validateOfUserCouponFindById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserCouponId()).isEqualTo(1L);
    }
}