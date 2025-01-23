package kr.hhplus.be.server.domain.order;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository {
    List<OrderDetail> findAllByProductIds(List<Long> productIds);
    List<OrderDetail> findAllByOrderIdWithLock(Long orderId);
}
