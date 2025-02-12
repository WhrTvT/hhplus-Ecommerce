package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.application.in.PaymentCommand;
import kr.hhplus.be.server.domain.order.OrderValidator;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.user.UserWallet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentValidator paymentValidator;
    private final ProductQuantityUpdater productQuantityUpdater;
    private final OrderValidator orderValidator;

    @Transactional
    public Payment payment(PaymentCommand paymentCommand) {
        // 이미 결제된 주문인지 확인
        paymentValidator.validateOfAlready(paymentCommand.orderId());

        // 존재하는 주문인지
        Orders orders = orderValidator.validateOfOrderFindById(paymentCommand.orderId());

        // 잔액이 충분한지
        UserWallet userWallet = paymentValidator.validateOfUserWalletFindByUserId(orders.getUserId());

        // 잔액 차감
        userWallet.usedAmount(orders.getFinalPrice());

        // 결제 객체 생성
        Payment savedPayment;

        // 재고 차감 성공/실패
        if(productQuantityUpdater.updateProductQuantity(orders.getOrderId())){
            // 결제 성공
            savedPayment = paymentRepository.save(Payment.builder()
                    .orderId(paymentCommand.orderId())
                    .method(paymentCommand.method())
                    .status(String.valueOf(PaymentStatus.SUCCESS))
                    .paymentAt(paymentCommand.paymentAt())
                    .orders(orders)
                    .build());
        } else{
            // 결제 실패
            savedPayment = paymentRepository.save(Payment.builder()
                    .orderId(paymentCommand.orderId())
                    .method(paymentCommand.method())
                    .status(String.valueOf(PaymentStatus.FAIL))
                    .paymentAt(paymentCommand.paymentAt())
                    .orders(orders)
                    .build());
        }

        // 통계로그 : 결제 시, 사용한 방법
        log.info("orderId: {}, method: {}, status: {}", orders.getOrderId(), paymentCommand.method(), savedPayment.getStatus());

        return savedPayment;
    }
}