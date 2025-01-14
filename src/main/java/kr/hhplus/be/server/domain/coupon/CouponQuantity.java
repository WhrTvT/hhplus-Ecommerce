package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
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
public class CouponQuantity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long couponQuantityId;

    @Column(insertable=false, updatable=false)
    private long couponId;

    private long quantity;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "CouponId")
    private Coupon coupon;

    @Builder
    public CouponQuantity(long couponId, long quantity){
        this.couponId = couponId;
        this.quantity = quantity;
    }

    public void couponIssued() {
        this.quantity -= 1;
    }
}
