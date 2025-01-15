package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
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
public class OrderDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderDetailId;

    @Column(insertable = false, updatable = false)
    private long userId;

    @Column(insertable=false, updatable=false)
    private long orderId;

    @Column(insertable=false, updatable=false)
    private long productId;

    @NotBlank
    private long selectQuantity;

    @NotBlank
    private BigDecimal unitPrice;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
