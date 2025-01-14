package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @Column(length=50)
    @NotBlank
    private String name;

    @Column(length=256)
    private String detail;

    @NotBlank
    private BigDecimal price;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Product(
            String name,
            String detail,
            BigDecimal price,
            long quantity
    ) {
        this.name = name;
        this.detail = detail;
        this.price = price;
        ProductStock.builder()
                .quantity(quantity)
                .build();
    }
}
