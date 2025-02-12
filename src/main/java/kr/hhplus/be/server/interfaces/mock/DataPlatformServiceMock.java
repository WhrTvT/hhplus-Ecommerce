package kr.hhplus.be.server.interfaces.mock;

import kr.hhplus.be.server.application.out.PaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataPlatformServiceMock {
    public String sendPaymentToMockPlatform(PaymentInfo paymentInfo) {
        log.info("sendPaymentToMockPlatform : {}", paymentInfo);

        return "데이터플랫폼 전송 성공";
    }
}
