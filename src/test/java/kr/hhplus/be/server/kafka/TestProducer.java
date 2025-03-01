package kr.hhplus.be.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class TestProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        log.debug("send message: {}, topic: {}", message, topic);
        kafkaTemplate.send(topic, message);
    }
}
