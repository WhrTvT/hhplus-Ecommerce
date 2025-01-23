package kr.hhplus.be.server.user.integration;

import kr.hhplus.be.server.application.in.PaymentCommand;
import kr.hhplus.be.server.application.in.UserWalletChargeCommand;
import kr.hhplus.be.server.common.IntegrationTest;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.UserWallet;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserWalletJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class UserWalletConcurrency extends IntegrationTest {

    @Autowired
    private UserWalletJpaRepository userWalletJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void init(){
        userWalletJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("🟢 10번의 동시 충전이 모두 정상적으로 처리된다.")
    void charge() throws ExecutionException, InterruptedException {
        // given
        User user = userJpaRepository.save(User.builder().name("장수현").build());
        BigDecimal chargeAmount = new BigDecimal("10000");

        userWalletJpaRepository.save(UserWallet.builder().userId(user.getUserId()).currentAmount(new BigDecimal("0")).user(user).build());

        int threadCount = 10;
        final List<CompletableFuture<Boolean>> tasks = new ArrayList<>(threadCount);
        final AtomicInteger exceptionCount = new AtomicInteger(0);

        // when
        for (int i = 1; i <= threadCount; i++) {
            tasks.add(CompletableFuture.supplyAsync(() -> {
                userService.charge(UserWalletChargeCommand.of(user.getUserId(), chargeAmount));
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

        assertThat(exceptionCount.get()).isEqualTo(0);
        assertThat(successCount).isEqualTo(10);
        assertThat(failCount).isEqualTo(exceptionCount.get());
    }
}
