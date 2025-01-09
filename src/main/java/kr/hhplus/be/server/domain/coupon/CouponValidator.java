package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.BusinessLogicException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponValidator {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    public UserCoupon validateOfUserCouponFindById(long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COUPON_NOT_FOUND));
    }

    public Coupon validateOfCouponFindById(long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COUPON_NOT_FOUND));
    }

    public CouponQuantity validateOfFindCouponQuantityById(long couponId) {
        return couponRepository.findCouponQuantityById(couponId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUANTITY_NOT_FOUND));
    }

    public void validateOfQuantityIsZERO(long quantity) {
        if(quantity <= 0){
            throw new BusinessLogicException(ExceptionCode.COUPON_MAX_ISSUED);
        }
    }
}
