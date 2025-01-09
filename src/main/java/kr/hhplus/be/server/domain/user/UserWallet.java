package kr.hhplus.be.server.domain.user;

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
public class UserWallet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long walletId;

    @Column(insertable=false, updatable=false)
    private long userId;

    @NotBlank
    private BigDecimal currentAmount;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne // 1:1
    @JoinColumn(name = "userId") // User.walletId 조인
    private User user;

    public void chargeAmount(BigDecimal chargeAmount) {
        this.currentAmount = this.currentAmount.add(chargeAmount);
    }

    public void usedAmount(BigDecimal usedAmount) {
        this.currentAmount = this.currentAmount.subtract(usedAmount);
    }
}
