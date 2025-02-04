package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long couponId;

    @NotNull
    private String couponName;

    @NotNull
    private BigDecimal discount;

    private boolean isPercent;

    @NotNull
    private LocalDateTime expiredAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public CouponQuantity setQuantity(long quantity) {
        return CouponQuantity.builder().quantity(quantity).build();
    }
}