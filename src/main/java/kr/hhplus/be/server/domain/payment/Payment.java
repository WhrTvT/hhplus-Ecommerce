package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.domain.order.Orders;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payment", indexes = {
        @Index(name = "idx_status_paymentAt_orderId", columnList = "status, paymentAt, orderId")
})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(insertable=false, updatable=false)
    private long orderId;

    @Column(length=50)
    @NotNull
    private String method;

    @Column(length=50)
    @NotNull
    private String status;

    @NotNull
    private LocalDateTime paymentAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne // 1:1
    @JoinColumn(name = "orderId")
    private Orders orders;
}
