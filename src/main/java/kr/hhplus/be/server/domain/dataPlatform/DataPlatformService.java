package kr.hhplus.be.server.domain.dataPlatform;

import kr.hhplus.be.server.application.out.PaymentInfo;
import kr.hhplus.be.server.domain.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataPlatformService {
    public String sendPaymentToMockPlatform(PaymentInfo paymentInfo) {
        log.info("sendPaymentToMockPlatform : {}", paymentInfo);

        return "데이터플랫폼 전송 성공";
    }

    public void sendPaymentInfo(Payment payment) {
        log.debug("Data send to DataPlatform");
        log.debug("sendPaymentInfo : {}", payment);
    }
}
