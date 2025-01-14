package kr.hhplus.be.server.interfaces.request;

import java.math.BigDecimal;

public record UserWalletChargeRequest(
        long userId,
        BigDecimal chargeAmount
) {

}
