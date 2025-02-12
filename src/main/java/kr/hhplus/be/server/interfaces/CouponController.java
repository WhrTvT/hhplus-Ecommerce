package kr.hhplus.be.server.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.application.CouponUseCase;
import kr.hhplus.be.server.application.out.CouponInfo;
import kr.hhplus.be.server.common.exception.ApiResponse;
import kr.hhplus.be.server.interfaces.request.CouponIssueRequest;
import kr.hhplus.be.server.interfaces.response.CouponIssueResponse;
import kr.hhplus.be.server.interfaces.response.CouponResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Coupon API", description = "쿠폰 발급과 쿠폰 조회를 관리합니다.")
@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
@Slf4j
public class CouponController {
    private final CouponUseCase couponUseCase;

    // 쿠폰 발급
    @Operation(summary = "쿠폰 발급", description = "Body로 받은 사용자 정보로 쿠폰을 발급합니다.")
    @Parameter(name = "couponIssuedRequest", description = "쿠폰 발급 Req 정보")
    @PatchMapping("/coupon")
//    public ApiResponse<CouponIssueResponse> CouponIssue(
    public ApiResponse<Boolean> CouponIssue(
            @Valid @RequestBody CouponIssueRequest couponIssueRequest
    ) {
//        CouponInfo couponInfo = couponUseCase.couponIssue(couponIssueRequest.couponId(), couponIssueRequest.userId(), couponIssueRequest.issueAt());
        Boolean couponInfo = couponUseCase.couponIssue(couponIssueRequest.couponId(), couponIssueRequest.userId(), couponIssueRequest.issueAt());
//        return ApiResponse.success(CouponIssueResponse.from(couponInfo));
        return ApiResponse.success(CouponIssueResponse.fromT(couponInfo));
    }

    // 쿠폰 조회
    @Operation(summary = "쿠폰 조회", description = "Path로 받은 유저ID로 쿠폰을 조회합니다")
    @Parameter(name = "walletId", description = "조회할 유저의 ID")
    @GetMapping("/mycoupon/{userId}")
    public ApiResponse<Page<CouponResponse>> UserCoupon(
            @PathVariable("userId") Long userId,
            Pageable pageable
    ) {
        Page<CouponInfo> couponInfo = couponUseCase.getUserCoupons(userId, pageable);
        return ApiResponse.success(couponInfo.map(CouponResponse::from));
    }
}