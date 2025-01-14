package kr.hhplus.be.server.interfaces.mock.request;

import java.math.BigDecimal;

public record UserWalletChargeRequestMock(
        long userId,
        BigDecimal chargeAmount
) {

}
