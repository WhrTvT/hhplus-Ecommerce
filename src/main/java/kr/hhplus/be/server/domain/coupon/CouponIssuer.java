package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// TODO - 대기열 처리 부분을 사용하도록 기존 로직 수정(현재는 그냥 비관락 사용중)
@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssuer {
    private final UserRepository userRepository;
    private final CouponValidator couponValidator;
    private final UserCouponRepository userCouponRepository;
    private final RedisTemplate<Long, Long> redisTemplate;

    // 대기열에 사용자 추가
    public boolean addQueue(User user, Coupon coupon) {
        // 남은 쿠폰 수량 체크
        couponValidator.validateOfFindCouponQuantityById(coupon.getCouponId());

        final long now = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(coupon.getCouponId(), user.getUserId(), now);
        log.info("Sorted set에 사용자 추가 - userId = {}, couponId = {}, {}ms", user.getUserId(), coupon.getCouponId(), now);

        // 사용자 쿠폰 발급 처리
        processQueue(user, coupon);

        return true;
    }

    // 대기열 처리
    public void processQueue(User user, Coupon coupon) {
        if(Boolean.TRUE.equals(redisTemplate.hasKey(coupon.getCouponId()))){
            throw new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "couponZset not found");
        }

        // 남은 쿠폰 수량 체크
        couponValidator.validateOfFindCouponQuantityById(coupon.getCouponId());

        // 쿠폰 발급 처리
        log.info("쿠폰 발급 처리 진행 - userId = {}, couponId = {}", user.getUserId(), coupon.getCouponId());
        publish(coupon);
    }

    // 쿠폰 발행과 발급 대기열에서 삭제/발행자 Set 등록
    public void publish(Coupon coupon){
        List<User> users = getUsersFromQueue(coupon.getCouponId());
        for (User user : users) {
            issueCoupon(user, coupon);
            decrementCoupon();
            redisTemplate.opsForZSet().remove(coupon.getCouponId(), user.getUserId());
            redisTemplate.opsForSet().add(coupon.getCouponId(), user.getUserId());
        }
    }

    // zSet에 저장된 사용자 목록 조회
    public List<User> getUsersFromQueue(Long zSetKey) {
        Set<Long> ids = redisTemplate.opsForZSet().range(zSetKey, 0, -1);
        List<User> users = new ArrayList<>();
        if (ids != null) {
            for (Long id : ids) {
                userRepository.findById(id).ifPresent(users::add);
            }
        }

        return users;
    }

    // 쿠폰 발급
    public void issueCoupon(User user, Coupon coupon){
        LocalDateTime issueAt = LocalDateTime.now();

        userCouponRepository.save(UserCoupon.builder()
                .couponId(coupon.getCouponId())
                .userId(user.getUserId())
                .status(String.valueOf(UserCouponStatus.UNUSED))
                .issueAt(issueAt)
                .coupon(coupon)
                .user(user)
                .build());

        log.info("'{}' 유저에게 '{}' 쿠폰 발급", user.getName(), coupon.getCouponName());
    }

    // 쿠폰 감소
    public void decrementCoupon(){
        CouponQuantity couponQuantity = new CouponQuantity();
        couponQuantity.couponIssued();
    }

}
