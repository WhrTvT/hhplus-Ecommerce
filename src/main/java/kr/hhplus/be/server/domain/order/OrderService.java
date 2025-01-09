package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.application.in.OrdersCommand;
import kr.hhplus.be.server.common.exception.BusinessLogicException;
import kr.hhplus.be.server.common.exception.ExceptionCode;
import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.order.response.OrderDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderValidator orderValidator;
    private final CouponValidator couponValidator;

    @Transactional
    public Orders order(OrdersCommand ordersCommand) {
        // 1. 요청에 포함된 OrderDetail 조회
        List<OrderDetail> selectedOrderDetails = orderDetailRepository.findAllByProductIds(
                ordersCommand.orderDetailLists().stream()
                        .map(OrderDetailDTO::productId) // OrderDetail의 productId로 조회
                        .toList()
        );

        orderValidator.validateOfOrderDetailIsEmpty(selectedOrderDetails);

        // 2. Orders 생성
        UserCoupon userCoupon = couponValidator.validateOfUserCouponFindById(ordersCommand.userCouponId());
        Coupon coupon = couponValidator.validateOfCouponFindById(userCoupon.getCouponId());

        Orders orders = new Orders();
        BigDecimal totalPrice = orders.getTotalPrice(selectedOrderDetails);
        BigDecimal couponDiscount = userCoupon.getCouponDiscount(totalPrice, coupon.getDiscount(), coupon.is_percent());
        BigDecimal finalPrice = orders.getFinalPrice(totalPrice, couponDiscount);

        orders = Orders.builder()
                .userId(ordersCommand.userId())
                .userCouponId(ordersCommand.userCouponId())
                .couponDiscount(couponDiscount)
                .totalPrice(totalPrice)
                .finalPrice(finalPrice)
                .build();

        // 3. Orders 저장
        Orders savedOrders = ordersRepository.save(orders);

        return savedOrders;
    }
}
