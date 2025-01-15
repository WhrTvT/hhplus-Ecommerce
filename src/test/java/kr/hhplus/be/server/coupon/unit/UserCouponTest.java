package kr.hhplus.be.server.coupon.unit;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponStatus;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCouponTest {
    @Test
    @DisplayName("UserCoupon 객체 생성")
    void createUserCoupon(){
        // given
        long couponId = 1;
        long userId = 1;
        String status = String.valueOf(UserCouponStatus.UNUSED);
        LocalDateTime issueAt = LocalDateTime.now();

        // when
        UserCoupon userCoupon = Instancio.of(UserCoupon.class)
                .set(Select.field("couponId"), couponId)
                .set(Select.field("userId"), userId)
                .set(Select.field("status"), status)
                .set(Select.field("issueAt"), issueAt)
                .create();

        // then
        assertThat(userCoupon.getCouponId()).isEqualTo(couponId);
        assertThat(userCoupon.getUserId()).isEqualTo(userId);
        assertThat(userCoupon.getStatus()).isEqualTo(status);
        assertThat(userCoupon.getIssueAt()).isEqualTo(issueAt);
    }
}
