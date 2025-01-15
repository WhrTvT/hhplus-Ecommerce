package kr.hhplus.be.server.user.unit;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserWallet;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class UserWalletTest {
    @Test
    @DisplayName("UserWallet 객체 생성")
    void createUserWallet(){
        //given
        long userId = 1;
        BigDecimal currentAmount = new BigDecimal("10000");

        //when
        UserWallet userWallet = Instancio.of(UserWallet.class)
                .set(Select.field("userId"), userId)
                .set(Select.field("currentAmount"), currentAmount)
                .create();

        //then
        assertThat(userWallet.getUserId()).isEqualTo(userId);
        assertThat(userWallet.getCurrentAmount()).isEqualTo(currentAmount);
    }

    @Test
    @DisplayName("UserWallet 충전")
    void chargeAmount(){
        //given
        long userId = 1;
        BigDecimal currentAmount = new BigDecimal("10000");
        BigDecimal chargeAmount  = new BigDecimal("10000");
        BigDecimal totalAmount  = new BigDecimal("20000");

        //when
        UserWallet userWallet = Instancio.of(UserWallet.class)
                .set(Select.field("userId"), userId)
                .set(Select.field("currentAmount"), currentAmount)
                .create();

        userWallet.chargeAmount(chargeAmount);

        //then
        assertThat(userWallet.getUserId()).isEqualTo(userId);
        assertThat(userWallet.getCurrentAmount()).isEqualTo(totalAmount);
    }

    @Test
    @DisplayName("UserWallet 사용")
    void useAmount(){
        //given
        long userId = 1;
        BigDecimal currentAmount = new BigDecimal("10000");
        BigDecimal usedAmount  = new BigDecimal("10000");
        BigDecimal totalAmount  = new BigDecimal("0");

        //when
        UserWallet userWallet = Instancio.of(UserWallet.class)
                .set(Select.field("userId"), userId)
                .set(Select.field("currentAmount"), currentAmount)
                .create();

        userWallet.usedAmount(usedAmount);

        //then
        assertThat(userWallet.getUserId()).isEqualTo(userId);
        assertThat(userWallet.getCurrentAmount()).isEqualTo(totalAmount);
    }
}
