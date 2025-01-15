package kr.hhplus.be.server.coupon.unit;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponQuantity;
import kr.hhplus.be.server.domain.order.Orders;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponTest {

    @Test
    @DisplayName("Coupon 객체 생성")
    void createCoupon(){
        //given
        long couponId = 1;
        String couponName = "first Coupon";
        BigDecimal discount = new BigDecimal("10");
        boolean isPercent = true;
        LocalDateTime expiredAt = LocalDateTime.now();
        long quantity = 10L;

        //when
        Coupon coupon = Instancio.of(Coupon.class)
                .set(Select.field("couponId"), couponId)
                .set(Select.field("couponName"), couponName)
                .set(Select.field("discount"), discount)
                .set(Select.field("isPercent"), isPercent)
                .set(Select.field("expiredAt"), expiredAt)
                .create();

        CouponQuantity couponQuantity = coupon.setQuantity(quantity);

        //then
        assertThat(coupon.getCouponId()).isEqualTo(couponId);
        assertThat(coupon.getCouponName()).isEqualTo(couponName);
        assertThat(coupon.getDiscount()).isEqualTo(discount);
        assertThat(coupon.isPercent()).isEqualTo(isPercent);
        assertThat(coupon.getExpiredAt()).isEqualTo(expiredAt);
        assertThat(couponQuantity.getQuantity()).isEqualTo(quantity);
    }
}
