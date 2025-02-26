package kr.hhplus.be.server.domain.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {
    private final OutboxRepository outboxRepository;

    @Transactional
    public Outbox save(Outbox outbox) {
        return outboxRepository.save(outbox);
    }

    public Outbox findById(String outboxId) {
        return outboxRepository.findById(outboxId);
    }
}
