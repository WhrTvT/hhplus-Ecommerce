package kr.hhplus.be.server.interfaces.event;

import kr.hhplus.be.server.domain.outbox.Outbox;
import kr.hhplus.be.server.domain.outbox.OutboxRepository;
import kr.hhplus.be.server.domain.outbox.OutboxService;
import kr.hhplus.be.server.domain.payment.event.PaymentSuccessEvent;
import kr.hhplus.be.server.interfaces.support.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    // outbox 저장
    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void saveOutbox(PaymentSuccessEvent paymentSuccessEvent) {
        outboxService.save(Outbox.init("PAYMENT", paymentSuccessEvent.getPaymentData()));
    }

    // 메시지 발행
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void sendPaymentInfo(PaymentSuccessEvent paymentSuccessEvent) {
        kafkaProducer.send("PAYMENT", paymentSuccessEvent.getOutboxId(), paymentSuccessEvent.getPaymentData());
    }
}
