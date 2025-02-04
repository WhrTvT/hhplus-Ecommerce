package kr.hhplus.be.server.infrastructure.orders;

import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderDetailImplRepository implements OrderDetailRepository {
    private final OrderDetailJpaRepository orderDetailJpaRepository;


    @Override
    public List<OrderDetail> findAllByProductIds(List<Long> productIds) {
        return orderDetailJpaRepository.findAllById(productIds);
    }

    @Override
    @Transactional
    public List<OrderDetail> findAllByOrderIdWithLock(Long orderId) {
        return orderDetailJpaRepository.findAllByOrderId(orderId);
    }
}
