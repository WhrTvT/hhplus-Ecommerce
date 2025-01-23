package kr.hhplus.be.server.infrastructure.product;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {
//    @Query("SELECT p.productId, p.name, p.detail, p.price, ps.quantity FROM Product p INNER JOIN ProductStock ps ON p.productId = ps.productId")
    @Query("SELECT new kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO(" +
        "p.productId, p.name, p.detail, p.price, ps.quantity) " +
        "FROM Product p INNER JOIN ProductStock ps ON p.productId = ps.productId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Page<ProductWithProductStockDTO> findProductWithStock(Pageable pageable);
}