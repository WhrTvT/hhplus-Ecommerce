package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.interfaces.support.exception.CustomException;
import kr.hhplus.be.server.interfaces.support.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderValidator {
    private final OrdersRepository ordersRepository;

    public Orders validateOfOrderFindById(long orderId) {
        return ordersRepository.findByIdWithLock(orderId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "Order not found"));
    }

    public List<OrderDetail> validateOfOrderDetailIsEmpty(List<OrderDetail> orderDetailList) {
        if (orderDetailList.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_SERVICE, LogLevel.WARN, "Order Detail not found");
        } else {
            return orderDetailList;
        }
    }
}
