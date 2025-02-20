package kr.hhplus.be.server.payment.integration;

import kr.hhplus.be.server.application.in.PaymentCommand;
import kr.hhplus.be.server.application.out.PaymentInfo;
import kr.hhplus.be.server.common.IntegrationTest;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
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
import kr.hhplus.be.server.interfaces.mock.DataPlatformServiceMock;
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

import static org.assertj.core.api.Assertions.*;

class PaymentConcurrencyTest extends IntegrationTest {

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

    @Autowired
    private DataPlatformServiceMock dataPlatformServiceMock;

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
    @DisplayName("🟢 결제를 동시에 10번 요청해도 1번의 결제만 성공한다.")
    void paymentConcurrency() throws ExecutionException, InterruptedException {
        // given
        User user = userJpaRepository.save(User.builder().name("장수현").build());
        userWalletJpaRepository.save(UserWallet.builder().userId(user.getUserId()).currentAmount(new BigDecimal("1000000")).user(user).build());

        Product product1 = productJpaRepository.save(Product.builder().name("상품1").detail("상품상세1").price(new BigDecimal("1000")).build());
        Product product2 = productJpaRepository.save(Product.builder().name("상품2").detail("상품상세2").price(new BigDecimal("2000")).build());
        Product product3 = productJpaRepository.save(Product.builder().name("상품3").detail("상품상세3").price(new BigDecimal("3000")).build());

        Orders order = ordersJpaRepository.save(Orders.builder().userId(user.getUserId()).totalPrice(new BigDecimal("213000")).finalPrice(new BigDecimal("213000")).user(user).build());

        productStockJpaRepository.save(
                ProductStock.builder()
                        .productId(product1.getProductId())
                        .quantity(10L)
                        .product(product1)
                        .build());
        productStockJpaRepository.save(
                ProductStock.builder()
                        .productId(product2.getProductId())
                        .quantity(100L)
                        .product(product2)
                        .build());
        productStockJpaRepository.save(
                ProductStock.builder()
                        .productId(product3.getProductId())
                        .quantity(30L)
                        .product(product3)
                        .build());

        orderDetailJpaRepository.save(
                OrderDetail.builder()
                        .userId(user.getUserId())
                        .orderId(order.getOrderId())
                        .productId(product1.getProductId())
                        .selectQuantity(10L)
                        .unitPrice(new BigDecimal("1000"))
                        .orders(order)
                        .product(product1)
                        .user(user)
                        .build());
        orderDetailJpaRepository.save(
                OrderDetail.builder()
                        .userId(user.getUserId())
                        .orderId(order.getOrderId())
                        .productId(product2.getProductId())
                        .selectQuantity(100L)
                        .unitPrice(new BigDecimal("2000"))
                        .orders(order)
                        .product(product2)
                        .user(user)
                        .build());
        orderDetailJpaRepository.save(
                OrderDetail.builder()
                        .userId(user.getUserId())
                        .orderId(order.getOrderId())
                        .productId(product2.getProductId())
                        .selectQuantity(1L)
                        .unitPrice(new BigDecimal("3000"))
                        .orders(order)
                        .product(product3)
                        .user(user)
                        .build());

        int threadCount = 10;
        final List<CompletableFuture<Boolean>> tasks = new ArrayList<>(threadCount);
        final AtomicInteger exceptionCount = new AtomicInteger(0);

        // when
        for (int i = 1; i <= threadCount; i++) {
            tasks.add(CompletableFuture.supplyAsync(() -> {
                paymentService.payment(PaymentCommand.of(order.getOrderId(), String.valueOf(PaymentMethod.CASH), LocalDateTime.now()));
                return true;
            }).exceptionally(e -> {
                if (e.getCause() instanceof CustomException) {
                    exceptionCount.incrementAndGet();
                }
                return false;
            }));
        }

        // then
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

        int successCount = 0;
        int failCount = 0;
        for (CompletableFuture<Boolean> task : tasks) {
            if (task.get()) {
                successCount++;
            } else {
                failCount++;
            }
        }

        assertThat(exceptionCount.get()).isEqualTo(9);
        assertThat(successCount).isEqualTo(1);
        assertThat(failCount).isEqualTo(exceptionCount.get());
    }

    @Test
    @DisplayName("🟢 결제를 1번 요청하면 성공한다.")
    void payment() {
        // given
        User user = userJpaRepository.save(User.builder().name("장수현").build());
        userWalletJpaRepository.save(UserWallet.builder().userId(user.getUserId()).currentAmount(new BigDecimal("1000000")).user(user).build());

        Product product1 = productJpaRepository.save(Product.builder().name("상품1").detail("상품상세1").price(new BigDecimal("1000")).build());
        Product product2 = productJpaRepository.save(Product.builder().name("상품2").detail("상품상세2").price(new BigDecimal("2000")).build());
        Product product3 = productJpaRepository.save(Product.builder().name("상품3").detail("상품상세3").price(new BigDecimal("3000")).build());

        Orders order = ordersJpaRepository.save(Orders.builder().userId(user.getUserId()).totalPrice(new BigDecimal("213000")).finalPrice(new BigDecimal("213000")).user(user).build());

        productStockJpaRepository.save(
                ProductStock.builder()
                        .productId(product1.getProductId())
                        .quantity(10L)
                        .product(product1)
                        .build());
        productStockJpaRepository.save(
                ProductStock.builder()
                        .productId(product2.getProductId())
                        .quantity(100L)
                        .product(product2)
                        .build());
        productStockJpaRepository.save(
                ProductStock.builder()
                        .productId(product3.getProductId())
                        .quantity(30L)
                        .product(product3)
                        .build());

        orderDetailJpaRepository.save(
                OrderDetail.builder()
                        .userId(user.getUserId())
                        .orderId(order.getOrderId())
                        .productId(product1.getProductId())
                        .selectQuantity(10L)
                        .unitPrice(new BigDecimal("1000"))
                        .orders(order)
                        .product(product1)
                        .user(user)
                        .build());
        orderDetailJpaRepository.save(
                OrderDetail.builder()
                        .userId(user.getUserId())
                        .orderId(order.getOrderId())
                        .productId(product2.getProductId())
                        .selectQuantity(100L)
                        .unitPrice(new BigDecimal("2000"))
                        .orders(order)
                        .product(product2)
                        .user(user)
                        .build());
        orderDetailJpaRepository.save(
                OrderDetail.builder()
                        .userId(user.getUserId())
                        .orderId(order.getOrderId())
                        .productId(product2.getProductId())
                        .selectQuantity(1L)
                        .unitPrice(new BigDecimal("3000"))
                        .orders(order)
                        .product(product3)
                        .user(user)
                        .build());

        Payment result = paymentService.payment(PaymentCommand.of(order.getOrderId(), String.valueOf(PaymentMethod.CASH), LocalDateTime.now()));

        // then
        assertThat(result).isNotNull(); // 결제 객체가 반환되었는지 확인
        assertThat(result.getStatus()).isEqualTo(String.valueOf(PaymentStatus.SUCCESS)); // 결제 상태가 SUCCESS인지 확인
    }

    @Test
    @DisplayName("🟢 결제 완료 시, 데이터플랫폼으로 데이터를 전송한다.")
    void dataPlatform(){
        // given
        Payment payment = paymentJpaRepository.save(
                Payment.builder()
                        .orderId(1)
                        .method(String.valueOf(PaymentMethod.CARD))
                        .status(String.valueOf(PaymentStatus.SUCCESS))
                        .paymentAt(LocalDateTime.now())
                        .build());

        // when
        PaymentInfo paymentInfo = PaymentInfo.from(payment);
        String dataPlatformResponse = dataPlatformServiceMock.sendPaymentToMockPlatform(paymentInfo);

        // then
        assertThat(dataPlatformResponse).isNotEmpty();
    }
}
