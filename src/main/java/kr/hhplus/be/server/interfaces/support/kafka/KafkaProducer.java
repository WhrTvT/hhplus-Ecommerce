package kr.hhplus.be.server.interfaces.support.kafka;

import kr.hhplus.be.server.interfaces.support.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String outboxId, Object message) {
        kafkaTemplate.send(topic, outboxId, JsonUtil.objectToString(message)).whenComplete((result, throwable) -> {
//        kafkaTemplate.send(topic, outboxId, (String) message).whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("[Produce Fail] message: {}", throwable.getMessage(), throwable);
            } else {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.info("[Produce Success] topic:{}, value: {}, offset: {}",
                        recordMetadata.topic(),
                        result.getProducerRecord().value(),
                        recordMetadata.offset());
            }
        });
    }
}
