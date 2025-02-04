package kr.hhplus.be.server.product.integration;

import kr.hhplus.be.server.common.IntegrationTest;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.orders.OrderDetailJpaRepository;
import kr.hhplus.be.server.infrastructure.orders.OrdersJpaRepository;
import kr.hhplus.be.server.infrastructure.payment.PaymentJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductStockJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ProductIntegrationTest extends IntegrationTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductStockJpaRepository productStockJpaRepository;

    @Autowired
    private OrdersJpaRepository ordersJpaRepository;

    @Autowired
    private OrderDetailJpaRepository orderDetailJpaRepository;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void init(){
        orderDetailJpaRepository.deleteAllInBatch();
        paymentJpaRepository.deleteAllInBatch();
        ordersJpaRepository.deleteAllInBatch();

        productStockJpaRepository.deleteAllInBatch();
        productJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("🟢 상품 전체 목록을 조회한다.")
    void getProducts(){
        // given
        int page = 1;
        int size = 5;
        String sort = "name";
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sort));

        Product product1 = productJpaRepository.save(Product.builder().name("상품1").detail("상품상세1").price(new BigDecimal("1000")).build());
        Product product2 = productJpaRepository.save(Product.builder().name("상품2").detail("상품상세2").price(new BigDecimal("2000")).build());
        Product product3 = productJpaRepository.save(Product.builder().name("상품3").detail("상품상세3").price(new BigDecimal("3000")).build());

        productStockJpaRepository.save(ProductStock.builder().productId(product1.getProductId()).quantity(10L).product(product1).build());
        productStockJpaRepository.save(ProductStock.builder().productId(product2.getProductId()).quantity(20L).product(product2).build());
        productStockJpaRepository.save(ProductStock.builder().productId(product3.getProductId()).quantity(30L).product(product3).build());

        // when
        Page<ProductWithProductStockDTO> products = productService.findProducts(pageable);

        // then
        assertThat(products.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("🟢 최근 5일간 인기 상품 2위까지의 상품을 조회한다.")
    void getTopProducts(){
        // given
        long top = 2;
        long day = 5;

        User user = userJpaRepository.save(User.builder().name("장수현").build());

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
                        .quantity(20L)
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
                        .selectQuantity(10)
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
                        .selectQuantity(100)
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
                        .selectQuantity(1)
                        .unitPrice(new BigDecimal("3000"))
                        .orders(order)
                        .product(product3)
                        .user(user)
                        .build());

        paymentJpaRepository.save(
                Payment.builder()
                        .orderId(order.getOrderId())
                        .method(String.valueOf(PaymentMethod.CARD))
                        .status(String.valueOf(PaymentStatus.SUCCESS))
                        .paymentAt(LocalDateTime.now())
                        .orders(order)
                        .build());

        // when
        List<ProductWithProductStockDTO> topProducts = productService.findTopProducts(top, day);

        // then
        assertThat(topProducts.size()).isEqualTo(2);
    }
}
