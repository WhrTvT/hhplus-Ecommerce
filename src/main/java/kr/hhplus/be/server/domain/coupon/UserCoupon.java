package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String status;

    @NotNull
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
}