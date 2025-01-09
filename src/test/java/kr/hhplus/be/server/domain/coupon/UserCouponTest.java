package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.BusinessLogicException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class UserCouponTest {
    @Mock
    UserCouponRepository userCouponRepository;

    @InjectMocks
    CouponValidator couponValidator;

    @Test
    @DisplayName("🔴 유저 쿠폰이 존재하지 않으면 COUPON_NOT_FOUND 예외 발생")
    void testValidateOfUserCouponFindByIdThrowsException() {
        // given
        Mockito.when(userCouponRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponValidator.validateOfUserCouponFindById(1L))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessage(ExceptionCode.COUPON_NOT_FOUND.getMessage());
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