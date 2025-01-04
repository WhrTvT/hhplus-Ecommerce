package kr.hhplus.be.server.interfaces;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.interfaces.request.CouponIssuedRequest;
import kr.hhplus.be.server.interfaces.response.UserCouponIssuedResponse;
import kr.hhplus.be.server.interfaces.response.UserCouponResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
public class CouponController {
//    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);
//    private final CouponService couponService;

    // 쿠폰 발급
    @PatchMapping("/coupon")
    public ResponseEntity<UserCouponIssuedResponse> CouponIssued(
            @RequestBody CouponIssuedRequest couponIssuedRequest
    ) {
        return ResponseEntity.ok(UserCouponIssuedResponse.mock(couponIssuedRequest.couponId(), couponIssuedRequest.userId()));

    }

    // 쿠폰 조회
    @GetMapping("/mycoupon/{userId}")
    public ResponseEntity<UserCouponResponse> UserCoupon(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(UserCouponResponse.mock(userId));
    }
}