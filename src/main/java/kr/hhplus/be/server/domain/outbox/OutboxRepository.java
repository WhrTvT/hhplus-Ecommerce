package kr.hhplus.be.server.domain.outbox;

import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository {
    Outbox save(Outbox outbox);

    Outbox findById(String id);
}
