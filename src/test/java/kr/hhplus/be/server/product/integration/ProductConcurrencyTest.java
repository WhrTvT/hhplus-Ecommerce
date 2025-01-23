package kr.hhplus.be.server.product.integration;

import kr.hhplus.be.server.application.in.PaymentCommand;
import kr.hhplus.be.server.common.IntegrationTest;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserWallet;
import kr.hhplus.be.server.infrastructure.orders.OrderDetailJpaRepository;
import kr.hhplus.be.server.infrastructure.orders.OrdersJpaRepository;
import kr.hhplus.be.server.infrastructure.payment.PaymentJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductStockJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserWalletJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductConcurrencyTest extends IntegrationTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductStockJpaRepository productStockJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserWalletJpaRepository userWalletJpaRepository;

    @Autowired
    private OrdersJpaRepository ordersJpaRepository;

    @Autowired
    private OrderDetailJpaRepository orderDetailJpaRepository;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private PaymentService paymentService;

    @BeforeEach
    void init(){
        orderDetailJpaRepository.deleteAllInBatch();
        ordersJpaRepository .deleteAllInBatch();

        userWalletJpaRepository.deleteAllInBatch();
        userJpaRepository .deleteAllInBatch();

        paymentJpaRepository.deleteAllInBatch();

        productStockJpaRepository.deleteAllInBatch();
        productJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("🟢 다수의 유저가 동일한 제품을 재고보다 많이 주문할 경우, 먼저 결제한 유저만 성공한다.")
    void productStockConcurrency() throws ExecutionException, InterruptedException {
        // given
        User user1 = userJpaRepository.save(User.builder().name("장수현").build());
        User user2 = userJpaRepository.save(User.builder().name("김수현").build());

        userWalletJpaRepository.save(UserWallet.builder().userId(user1.getUserId()).currentAmount(new BigDecimal("100000")).user(user1).build());
        userWalletJpaRepository.save(UserWallet.builder().userId(user2.getUserId()).currentAmount(new BigDecimal("200000")).user(user2).build());

        Orders order1 = ordersJpaRepository.save(Orders.builder().userId(user1.getUserId()).totalPrice(new BigDecimal("100000")).finalPrice(new BigDecimal("100000")).user(user1).build());
        Orders order2 = ordersJpaRepository.save(Orders.builder().userId(user2.getUserId()).totalPrice(new BigDecimal("200000")).finalPrice(new BigDecimal("200000")).user(user2).build());

        Product product1 = productJpaRepository.save(Product.builder().name("상품1").detail("상품상세1").price(new BigDecimal("10000")).build());

        productStockJpaRepository.save(
                ProductStock.builder()
                        .productId(product1.getProductId())
                        .quantity(10L)
                        .product(product1)
                        .build());

        orderDetailJpaRepository.save(
                OrderDetail.builder()
                        .userId(user1.getUserId())
                        .orderId(order1.getOrderId())
                        .productId(product1.getProductId())
                        .selectQuantity(5L)
                        .unitPrice(new BigDecimal("10000"))
                        .orders(order1)
                        .product(product1)
                        .user(user1)
                        .build());

        orderDetailJpaRepository.save(
                OrderDetail.builder()
                        .userId(user2.getUserId())
                        .orderId(order2.getOrderId())
                        .productId(product1.getProductId())
                        .selectQuantity(10L)
                        .unitPrice(new BigDecimal("10000"))
                        .orders(order2)
                        .product(product1)
                        .user(user2)
                        .build());

        int threadCount = 1;
        final List<CompletableFuture<Boolean>> tasks = new ArrayList<>(threadCount);
        final AtomicInteger exceptionCount = new AtomicInteger(0);

        // when
        for (int i = 1; i <= threadCount; i++) {
            tasks.add(CompletableFuture.supplyAsync(() -> {
                paymentService.payment(PaymentCommand.of(order1.getOrderId(), String.valueOf(PaymentMethod.CASH), LocalDateTime.now()));
                paymentService.payment(PaymentCommand.of(order2.getOrderId(), String.valueOf(PaymentMethod.CASH), LocalDateTime.now()));
                return true;
            }).exceptionally(e -> {
                if (e.getCause() instanceof CustomException) {
                    exceptionCount.incrementAndGet();
                }
                return false;
            }));
        }

        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

        Payment payment1 = paymentJpaRepository.findByOrderId(order1.getOrderId());
        Payment payment2 = paymentJpaRepository.findByOrderId(order2.getOrderId());

        // then
        int successCount = 0;
        int failCount = 0;
        for (CompletableFuture<Boolean> task : tasks) {
            if (task.get()) {
                successCount++;
            } else {
                failCount++;
            }
        }

        assertThat(payment1.getStatus()).isEqualTo("SUCCESS");
        assertThat(payment2.getStatus()).isEqualTo("FAIL");
    }
}