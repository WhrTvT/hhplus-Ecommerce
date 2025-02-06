package kr.hhplus.be.server.coupon.integration;

import kr.hhplus.be.server.application.in.CouponIssueCommand;
import kr.hhplus.be.server.application.in.PaymentCommand;
import kr.hhplus.be.server.common.IntegrationTest;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponQuantity;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserWallet;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.CouponQuantityJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CouponConcurrencyTest extends IntegrationTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponQuantityJpaRepository couponQuantityJpaRepository;

    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void init(){
        couponJpaRepository.deleteAllInBatch();
        couponQuantityJpaRepository.deleteAllInBatch();
        userCouponJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("🟢 쿠폰 발급이 동시에 요청되어도 재고량만큼만 발급된다.")
    void couponIssueConcurrency() throws ExecutionException, InterruptedException {
        // given
        User user1 = userJpaRepository.save(User.builder().name("장수현1").build());
        User user2 = userJpaRepository.save(User.builder().name("장수현2").build());
        User user3 = userJpaRepository.save(User.builder().name("장수현3").build());
        User user4 = userJpaRepository.save(User.builder().name("장수현4").build());
        User user5 = userJpaRepository.save(User.builder().name("장수현5").build());

        Coupon coupon = couponJpaRepository.save(Coupon.builder().couponName("10000원 정액 쿠폰").discount(new BigDecimal("10000")).isPercent(false).expiredAt(LocalDateTime.now()).build());
        couponQuantityJpaRepository.save(CouponQuantity.builder().couponId(coupon.getCouponId()).quantity(5).coupon(coupon).build());

        int threadCount = 10;
        final List<CompletableFuture<Boolean>> tasks = new ArrayList<>(threadCount);
        final AtomicInteger exceptionCount = new AtomicInteger(0);

        // when
        for (int i = 1; i <= threadCount; i++) {
            long userId = i;
            tasks.add(CompletableFuture.supplyAsync(() -> {
                couponService.issue(CouponIssueCommand.of(coupon.getCouponId(), userId, LocalDateTime.now()));
                return true;
            }).exceptionally(e -> {
                if (e.getCause() instanceof CustomException) {
                    exceptionCount.incrementAndGet();
                }
                return false;
            }));
        }

        // then
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

        int successCount = 0;
        int failCount = 0;
        for (CompletableFuture<Boolean> task : tasks) {
            if (task.get()) {
                successCount++;
            } else {
                failCount++;
            }
        }

        assertThat(exceptionCount.get()).isEqualTo(5);
        assertThat(successCount).isEqualTo(5);
        assertThat(failCount).isEqualTo(exceptionCount.get());
    }

    @Test
    @DisplayName("🟢 쿠폰 발급을 요청하면 발급된다.")
    void couponIssue() {
        // given
        User user = userJpaRepository.save(User.builder().name("장수현").build());
        Coupon coupon = couponJpaRepository.save(Coupon.builder().couponName("10000원 정액 쿠폰").discount(new BigDecimal("10000")).isPercent(false).expiredAt(LocalDateTime.now()).build());
        couponQuantityJpaRepository.save(CouponQuantity.builder().couponId(coupon.getCouponId()).quantity(5).coupon(coupon).build());

        // when
//        UserCoupon userCoupon = couponService.issue(CouponIssueCommand.of(coupon.getCouponId(), user.getUserId(), LocalDateTime.now()));
        Boolean userCoupon = couponService.issue(CouponIssueCommand.of(coupon.getCouponId(), user.getUserId(), LocalDateTime.now()));

        // then
        assertThat(userCoupon).isEqualTo(true);
    }
}
