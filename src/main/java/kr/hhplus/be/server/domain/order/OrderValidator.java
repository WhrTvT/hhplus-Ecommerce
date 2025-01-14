package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.exception.BusinessLogicException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderValidator {
    private final OrdersRepository ordersRepository;

    public Orders validateOfOrderFindById(long orderId) {
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
    }

    public List<OrderDetail> validateOfOrderDetailIsEmpty(List<OrderDetail> orderDetailList) {
        if (orderDetailList.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.ORDER_DETAIL_NOT_FOUND);
        } else {
            return orderDetailList;
        }
    }
}
