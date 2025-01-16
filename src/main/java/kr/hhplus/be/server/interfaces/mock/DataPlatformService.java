package kr.hhplus.be.server.interfaces.mock;

import kr.hhplus.be.server.application.out.PaymentInfo;
import org.springframework.stereotype.Service;

@Service
public class DataPlatformService {
    public String sendPaymentToMockPlatform(PaymentInfo paymentInfo) {

        return "데이터플랫폼 전송 성공";
    }
}
