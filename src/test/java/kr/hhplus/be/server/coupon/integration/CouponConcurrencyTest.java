package kr.hhplus.be.server.coupon.integration;

import kr.hhplus.be.server.application.in.CouponIssueCommand;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponQuantity;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CouponConcurrencyTest {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void testConcurrentCouponIssuance() throws InterruptedException {
        // Given: 쿠폰과 수량 설정
        long couponId = 1L;
        Coupon coupon = Coupon.builder()
                .couponName("Test Coupon")
                .discount(new BigDecimal("10000"))
                .isPercent(false)
                .expiredAt(LocalDateTime.now().plusDays(7))
                .build();
        couponRepository.save(coupon);

        CouponQuantity couponQuantity = CouponQuantity.builder()
                .couponId(coupon.getCouponId())
                .quantity(10L)
                .build();
        couponRepository.save(couponQuantity);

        // 동시성 테스트를 위한 쓰레드 설정
        int threadCount = 20; // 20개 쓰레드 시뮬레이션
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        IntStream.range(0, threadCount).forEach(i -> executor.submit(() -> {
                try {
                    CouponIssueCommand couponIssueCommand = new CouponIssueCommand(couponId, i, LocalDateTime.now());
                    couponService.issue(couponIssueCommand);
                } catch (Exception e) {
                    System.err.println("발급 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
        }));

        latch.await();
        executor.shutdown();

        // Then: 발급된 쿠폰 수량 확인
        CouponQuantity updatedQuantity = couponRepository.findCouponQuantityByIdWithLock(couponId)
                .orElseThrow(() -> new RuntimeException("쿠폰 수량 정보를 찾을 수 없습니다."));
        assertTrue(updatedQuantity.getQuantity() >= 0, "쿠폰 수량이 음수가 되었습니다!");
        assertEquals(0, updatedQuantity.getQuantity(), "쿠폰 수량이 예상과 다릅니다!");
    }
}
