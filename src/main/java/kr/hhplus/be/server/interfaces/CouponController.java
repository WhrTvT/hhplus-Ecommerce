package kr.hhplus.be.server.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.request.CouponIssuedRequest;
import kr.hhplus.be.server.interfaces.response.UserCouponIssuedResponse;
import kr.hhplus.be.server.interfaces.response.UserCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Coupon API", description = "쿠폰 발급과 쿠폰 조회를 관리합니다.")
@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
public class CouponController {
//    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);
//    private final CouponService couponService;

    // 쿠폰 발급
    @Operation(summary = "쿠폰 발급", description = "Body로 받은 사용자 정보로 쿠폰을 발급합니다.")
    @Parameter(name = "couponIssuedRequest", description = "쿠폰 발급 Req 정보")
    @PatchMapping("/coupon")
    public ResponseEntity<UserCouponIssuedResponse> CouponIssue(
            @RequestBody CouponIssuedRequest couponIssuedRequest
    ) {
        return ResponseEntity.ok(UserCouponIssuedResponse.mock(couponIssuedRequest.couponId(), couponIssuedRequest.userId()));

    }

    // 쿠폰 조회
    @Operation(summary = "쿠폰 조회", description = "Path로 받은 유저ID로 쿠폰을 조회합니다")
    @Parameter(name = "userId", description = "조회할 유저의 ID")
    @GetMapping("/mycoupon/{userId}")
    public ResponseEntity<UserCouponResponse> UserCoupon(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(UserCouponResponse.mock(userId));
    }
}