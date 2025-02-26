package kr.hhplus.be.server.domain.outbox;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Outbox {

    @Id
    private String outboxId;

    private String messageType;

    @Column(columnDefinition = "longtext")
    private String message;

    private String status;
    private long cnt;

    public static Outbox init(String messageType, String message) {
        return Outbox.builder()
                .outboxId(UUID.randomUUID().toString())
                .messageType(messageType)
                .message(message)
                .status("INIT")
                .cnt(0)
                .build();
    }

    public Outbox published() {
        this.status = String.valueOf(OutboxStatus.PUBLISHED);
        return this;
    }

    public Outbox failed() {
        this.status = String.valueOf(OutboxStatus.FAIL);
        return this;
    }

    public Outbox incrementCnt() {
        this.cnt = this.cnt + 1;
        return this;
    }
}