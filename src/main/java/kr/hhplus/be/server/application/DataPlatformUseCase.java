package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.dataPlatform.DataPlatformService;
import kr.hhplus.be.server.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataPlatformUseCase {
    private final DataPlatformService dataPlatformService;

    public void sendPaymentInfo(Payment payment) throws InterruptedException{
        dataPlatformService.sendPaymentInfo(payment);
    }
}
