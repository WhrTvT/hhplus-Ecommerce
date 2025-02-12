package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.application.in.CouponCommand;
import kr.hhplus.be.server.application.in.CouponIssueCommand;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final UserCouponRepository userCouponRepository;
    private final CouponValidator couponValidator;
    private final UserValidator userValidator;
    private final CouponIssuer couponIssuer;

    public Coupon getCoupon(CouponCommand couponCommand) {
        Coupon coupon = couponValidator.validateOfCouponFindById(couponCommand.couponId());
        CouponQuantity couponQuantity = couponValidator.validateOfFindCouponQuantityById(coupon.getCouponId());
        couponValidator.validateOfQuantityIsZERO(couponQuantity.getQuantity());

        return coupon;
    }

    @Transactional
    public Boolean issue(CouponIssueCommand couponIssueCommand) {
//    public UserCoupon issue(CouponIssueCommand couponIssueCommand) {
        Coupon coupon = couponValidator.validateOfCouponFindById(couponIssueCommand.couponId());

        User user = userValidator.validateOfUserFindById(couponIssueCommand.userId());

        // 중복 발급 여부 확인
        couponValidator.validateOfDuplicateIssueCoupon(user, coupon);

        // 대기열 처리
        Boolean issuedCoupon = couponIssuer.addQueue(user, coupon);

        return issuedCoupon;
    }

    public Page<UserCoupon> getUserCoupons(long userId, Pageable pageable) {
        userValidator.validateOfUserFindById(userId);

        return userCouponRepository.findAllByUserId(userId, pageable);
    }
}
