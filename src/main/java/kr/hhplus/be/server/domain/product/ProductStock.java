package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stockId;

    @Column(insertable=false, updatable=false)
    private long productId;

    @NotNull
    private long quantity;

    @CreatedDate
    private LocalDateTime createdAt; // 생성 날짜

    @LastModifiedDate
    private LocalDateTime updatedAt; // 업데이트 날짜

    @OneToOne
    @JoinColumn(name = "productId")
    private Product product;

    public void decrementalQuantity(long quantity) {
        this.quantity -= quantity;
    }
}
