package kr.hhplus.be.server.interfaces.consumer;

import kr.hhplus.be.server.application.DataPlatformUseCase;
import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.interfaces.support.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {
    private final OutboxService outboxService;
    private final DataPlatformUseCase dataPlatformUseCase;

    @KafkaListener(topics = "PAYMENT", groupId = "payment-outbox")
    public void checkOutbox(ConsumerRecord<String, String> record) {
        log.info("[payment-outbox] key: {}", record.key());
        Outbox outbox = outboxService.findById(record.key());
        outboxService.save(outbox.published());
    }

    @KafkaListener(topics = "PAYMENT", groupId = "payment-process")
    public void paymentDataExpired(ConsumerRecord<String, String> record) throws InterruptedException {
        log.info("[payment-process] value: {}", record.value());
        Payment payment = JsonUtil.stringToObject(record.value(), Payment.class);
        dataPlatformUseCase.sendPaymentInfo(payment);
    }
}