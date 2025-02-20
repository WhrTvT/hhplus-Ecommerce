package kr.hhplus.be.server.payment.integration;

import kr.hhplus.be.server.application.out.PaymentInfo;
import kr.hhplus.be.server.common.IntegrationTest;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.infrastructure.payment.PaymentJpaRepository;
import kr.hhplus.be.server.interfaces.mock.DataPlatformServiceMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DataPlatformTest extends IntegrationTest {


    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private DataPlatformServiceMock dataPlatformServiceMock;

    @BeforeEach
    void init(){
        paymentJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("🟢 결제 완료 시, 데이터플랫폼으로 데이터를 전송한다.")
    void dataPlatform(){
        // given
        Payment payment = paymentJpaRepository.save(
                Payment.builder()
                        .orderId(1)
                        .method(String.valueOf(PaymentMethod.CARD))
                        .status(String.valueOf(PaymentStatus.SUCCESS))
                        .paymentAt(LocalDateTime.now())
                        .build());

        // when
        PaymentInfo paymentInfo = PaymentInfo.from(payment);
        String dataPlatformResponse = dataPlatformServiceMock.sendPaymentToMockPlatform(paymentInfo);

        // then
        assertThat(dataPlatformResponse).isNotEmpty();
    }
}
