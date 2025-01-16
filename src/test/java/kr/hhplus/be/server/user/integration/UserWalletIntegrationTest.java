package kr.hhplus.be.server.user.integration;

import kr.hhplus.be.server.application.in.UserWalletChargeCommand;
import kr.hhplus.be.server.application.in.UserWalletCommand;
import kr.hhplus.be.server.common.IntegrationTest;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
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

import static org.assertj.core.api.Assertions.*;

public class UserWalletIntegrationTest extends IntegrationTest {

    @Autowired
    private UserWalletJpaRepository userWalletJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void init(){
        userJpaRepository.deleteAllInBatch();
        userWalletJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("🟢 UserWallet을 조회하고, 금액을 리턴한다.")
    void getUserWallet() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("10000.00");

        User user = userJpaRepository.save(User.builder().name("장수현").build());
        UserWallet userWallet = userWalletJpaRepository.save(UserWallet.builder().userId(user.getUserId()).currentAmount(amount).user(user).build());

        // when
        UserWallet result = userService.get(UserWalletCommand.from(userWallet.getUserId()));

        // then
        assertThat(result.getCurrentAmount()).isEqualTo(userWallet.getCurrentAmount());
    }

    @Test
    @DisplayName("🔴 존재하지 않는 사용자의 UserWallet을 조회하면 CustomException이 발생한다.")
    void getUserWalletIsNotFound() throws Exception {
        // given
        long userId = 1L;
        BigDecimal amount = new BigDecimal("10000.00");

        // when
        UserWallet userWallet = userWalletJpaRepository.save(UserWallet.builder().userId(userId).currentAmount(amount).build());

        // when
        // then
        assertThatThrownBy(() -> userService.get(UserWalletCommand.from(userWallet.getUserId())))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_SERVICE.getMessage());
    }

    @Test
    @DisplayName("🟢 UserWallet을 충전한다.")
    void chargeUserWallet() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("10000.00");
        BigDecimal chargeAmount = new BigDecimal("20000.00");
        BigDecimal totalAmount = new BigDecimal("30000.00");

        User user = userJpaRepository.save(User.builder().name("장수현").build());
        UserWallet userWallet = userWalletJpaRepository.save(UserWallet.builder().userId(user.getUserId()).currentAmount(amount).user(user).build());

        // when
        UserWallet result = userService.charge(UserWalletChargeCommand.of(userWallet.getUserId(), chargeAmount));

        // then
        assertThat(result.getCurrentAmount()).isEqualTo(userWallet.getCurrentAmount().add(chargeAmount));
    }

    @Test
    @DisplayName("🔴 존재하지 않는 사용자의 UserWallet을 충전하면 CustomException이 발생한다.")
    void chargeUserWalletIsNotFound() throws Exception {
        // given
        long userId = 1L;
        BigDecimal amount = new BigDecimal("10000.00");
        BigDecimal chargeAmount = new BigDecimal("20000.00");

        // when
        UserWallet userWallet = userWalletJpaRepository.save(UserWallet.builder().userId(userId).currentAmount(amount).build());

        // when
        // then
        assertThatThrownBy(() -> userService.charge(UserWalletChargeCommand.of(userWallet.getUserId(), chargeAmount)))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_SERVICE.getMessage());
    }

    @Test
    @DisplayName("🔴 충전 금액이 유효하지 않으면 CustomException이 발생한다.")
    void chargeUserWalletIsNotValid() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("10000.00");
        BigDecimal chargeAmount = new BigDecimal("-20000.00");
        BigDecimal totalAmount = new BigDecimal("30000.00");

        User user = userJpaRepository.save(User.builder().name("장수현").build());
        UserWallet userWallet = userWalletJpaRepository.save(UserWallet.builder().userId(user.getUserId()).currentAmount(amount).user(user).build());

        // when
        // then
        assertThatThrownBy(() -> userService.charge(UserWalletChargeCommand.of(userWallet.getUserId(), chargeAmount)))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.CONFLICT_SERVICE.getMessage());
    }
}
