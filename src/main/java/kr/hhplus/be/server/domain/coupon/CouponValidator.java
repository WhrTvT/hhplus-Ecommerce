package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponValidator {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    public UserCoupon validateOfUserCouponFindById(long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "Coupon not found"));
    }

    public Coupon validateOfCouponFindById(long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "Coupon not found"));
    }

    public CouponQuantity validateOfFindCouponQuantityById(long couponId) {
        return couponRepository.findCouponQuantityByIdWithLock(couponId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "Quantity not found"));
    }

    public void validateOfQuantityIsZERO(long quantity) {
        if(quantity <= 0){
            throw new CustomException(ExceptionCode.CONFLICT_SERVICE, LogLevel.WARN, "Coupon Max Issued");
        }
    }

    public void validateOfDuplicateIssueCoupon(User user, Coupon coupon){
        if(userCouponRepository.findByUserIdAndCouponId(user.getUserId(), coupon.getCouponId()).isPresent()){
            throw new CustomException(ExceptionCode.CONFLICT_SERVICE, LogLevel.WARN, "Duplicate coupon issued");
        }
    }
}