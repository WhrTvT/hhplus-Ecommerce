package kr.hhplus.be.server.infrastructure.product;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.product.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStockJpaRepository extends JpaRepository<ProductStock, Long> {

    Optional<ProductStock> findByProductId(long productId);

    @Query("SELECT ps FROM ProductStock ps INNER JOIN Product p ON p.productId = ps.productId WHERE p.productId = ?1")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProductStock> findByProductIdWithLock(long productId);
}
