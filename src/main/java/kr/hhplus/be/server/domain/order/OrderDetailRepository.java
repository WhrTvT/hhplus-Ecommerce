package kr.hhplus.be.server.domain.order;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository {
    List<OrderDetail> findAllByProductIds(List<Long> productIds);
}
