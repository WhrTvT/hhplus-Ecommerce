package kr.hhplus.be.server.payment.unit;

import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.payment.PaymentValidator;
import kr.hhplus.be.server.domain.user.UserWallet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PaymentValidatorTest {

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentValidator paymentValidator;

    @Test
    @DisplayName("🔴 결제에 필요한 지갑 정보가 존재하지 않으면 BusinessException 발생")
    void testUserWalletWithPaymentNotFound() {
        // given
        Mockito.when(paymentRepository.findUserWalletWithPaymentByOrderId(1L)).thenReturn(null);

        // when
        // then
        assertThatThrownBy(() -> {
            paymentValidator.validateOfFindUserWalletWithPaymentByOrderId(1L);
        }).isInstanceOf(CustomException.class).hasMessage("Payment Required");
    }

    @Test
    @DisplayName("🟢 결제에 필요한 지갑 정보가 존재하면 UserWallet 정보를 리턴")
    void testUserWalletWithPaymentFound() {
        // given
        UserWallet userWallet = UserWallet.builder().walletId(1L).userId(1L).currentAmount(new BigDecimal(100000)).build();
        Mockito.when(paymentRepository.findUserWalletWithPaymentByOrderId(1L)).thenReturn(userWallet);

        // when
        UserWallet wallet = paymentValidator.validateOfFindUserWalletWithPaymentByOrderId(1L);

        // then
        Assertions.assertThat(userWallet).isEqualTo(wallet);
    }
}
