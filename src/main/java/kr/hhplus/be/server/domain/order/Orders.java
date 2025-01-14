package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(insertable=false, updatable=false)
    private long userId;

    @Column(insertable=false, updatable=false)
    private long userCouponId;

    private BigDecimal couponDiscount;

    @NotBlank
    private BigDecimal totalPrice;

    @NotBlank
    private BigDecimal finalPrice;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "userCouponId")
    private UserCoupon userCoupon;

    public BigDecimal getTotalPrice(List<OrderDetail> orderDetails) {
        // 총 가격 계산
        return orderDetails.stream()
                .map(detail -> detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getSelect_quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getFinalPrice(BigDecimal totalPrice, BigDecimal couponDiscount) {
        // 최종 가격 계산
        return totalPrice.subtract(couponDiscount);
    }
}
