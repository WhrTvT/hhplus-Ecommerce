package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderDetailRepository;
import kr.hhplus.be.server.domain.order.response.OrderDetailDTO;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.product.ProductStockRepository;
import kr.hhplus.be.server.domain.product.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQuantityUpdater {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductStockRepository productStockRepository;
    private final ProductValidator productValidator;

    @Transactional
    public boolean updateProductQuantity(long orderId) {
        // 1. 주문 정보 조회
        List<OrderDetail> selectedOrderDetails = orderDetailRepository.findAllByOrderIdWithLock(orderId);

        // 2. 주문 제품 상세 조회
        List<OrderDetailDTO> orderDetails = selectedOrderDetails.stream()
                .map(orderDetail -> new OrderDetailDTO(
                        orderDetail.getUserId(),
                        orderDetail.getOrderId(),
                        orderDetail.getProductId(),
                        orderDetail.getSelectQuantity(),
                        orderDetail.getUnitPrice()
                ))
                .toList();

        // 3. 주문 제품 Quantity 차감
        for (OrderDetailDTO detail : orderDetails) {
            ProductStock productStock = productValidator.validateOfProductStockFindByProductId(detail.productId());

            // 현재 재고 확인 및 차감
            if(productValidator.validateOfProductStockQuantity(detail.selectQuantity(), productStock.getQuantity())){
                return false;
            }

            productStock.decrementalQuantity(
                    detail.selectQuantity()
            );

            // 재고 업데이트 저장
            productStockRepository.save(productStock);
        }

        return true;
    }
}
