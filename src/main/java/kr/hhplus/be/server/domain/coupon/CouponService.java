package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.application.in.CouponCommand;
import kr.hhplus.be.server.application.in.CouponIssueCommand;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponValidator couponValidator;
    private final UserValidator userValidator;

    public Coupon getCoupon(CouponCommand couponCommand) {
        couponValidator.validateOfUserCouponFindById(couponCommand.userId());

        Coupon coupon = couponValidator.validateOfCouponFindById(couponCommand.couponId());
        CouponQuantity couponQuantity = couponValidator.validateOfFindCouponQuantityById(coupon.getCouponId());
        couponValidator.validateOfQuantityIsZERO(couponQuantity.getQuantity());

        return coupon;
    }

    @Transactional
    public UserCoupon issue(CouponIssueCommand couponIssueCommand) {
        UserCoupon issuedCoupon = userCouponRepository.save(UserCoupon.builder()
                .couponId(couponIssueCommand.couponId())
                .userId(couponIssueCommand.userId())
                .status(String.valueOf(UserCouponStatus.UNUSED))
                .issueAt(couponIssueCommand.issueAt())
                .build());

        // 쿠폰량 감소
        CouponQuantity couponQuantity = new CouponQuantity();
        couponQuantity.couponIssued();

        return issuedCoupon;
    }

    public Page<UserCoupon> getUserCoupon(long userId, Pageable pageable) {
        userValidator.validateOfUserFindById(userId);

        return userCouponRepository.findByUserId(userId, pageable);
    }
}
