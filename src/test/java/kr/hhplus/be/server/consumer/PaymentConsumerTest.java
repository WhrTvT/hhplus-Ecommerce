package kr.hhplus.be.server.consumer;

import kr.hhplus.be.server.common.IntegrationTest;
import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxRepository;
import kr.hhplus.be.server.domain.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.interfaces.consumer.PaymentConsumer;
import kr.hhplus.be.server.interfaces.support.kafka.KafkaProducer;
import kr.hhplus.be.server.interfaces.support.util.JsonUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@Transactional
public class PaymentConsumerTest extends IntegrationTest {
    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    PaymentConsumer paymentConsumer;

    @Autowired
    OutboxRepository outboxRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @DisplayName("🟢 consume 성공 - outbox 상태가 정상적으로 변경 됨")
    @Test
    void consumeOutboxTest() {
        // given
        Payment payment = paymentRepository.save(Payment.builder()
                .orderId(1001L)
                .method("CARD")
                .status("SUCCESS")
                .paymentAt(LocalDateTime.now())
                .orders(null)
                .build());

        Outbox outbox = Outbox.init("PAYMENT", JsonUtil.objectToString(payment));
        outboxRepository.save(outbox);

        kafkaProducer.send("PAYMENT", outbox.getOutboxId(), payment);

        // when
        // then
        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(outboxRepository.findById(outbox.getOutboxId()).getStatus()).isEqualTo(OutboxStatus.PUBLISHED));
    }
}
