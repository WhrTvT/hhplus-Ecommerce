package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.hhplus.be.server.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserCoupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userCouponId;

    @Column(insertable=false, updatable=false)
    private long userId;

    @Column(insertable=false, updatable=false)
    private long couponId;

    @Column(length=50)
    @NotBlank
    private String status;

    @NotBlank
    private LocalDateTime issueAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "couponId")
    private Coupon coupon;

    @Builder
    public UserCoupon(
            long couponId,
            long userId,
            String status,
            LocalDateTime issueAt
    ){
        this.couponId = couponId;
        this.userId = userId;
        this.status = status;
        this.issueAt = issueAt;
    }

    public BigDecimal getCouponDiscount(BigDecimal totalPrice, BigDecimal discount, boolean isPercent) {
        if(isPercent){
            return totalPrice.divide(discount, 0, RoundingMode.HALF_UP);
        } else {
            return totalPrice.subtract(discount);
        }
    }
}