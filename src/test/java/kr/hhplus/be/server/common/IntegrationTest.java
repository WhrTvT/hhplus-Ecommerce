package kr.hhplus.be.server.common;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.CouponQuantityJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.orders.OrderDetailJpaRepository;
import kr.hhplus.be.server.infrastructure.orders.OrdersJpaRepository;
import kr.hhplus.be.server.infrastructure.payment.PaymentJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductStockJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserWalletJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    protected CouponService couponService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected PaymentService paymentService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected UserService userService;

    // coupon
    @Autowired
    protected CouponJpaRepository couponJpaRepository;
    @Autowired
    protected CouponQuantityJpaRepository couponQuantityJpaRepository;
    @Autowired
    protected UserCouponJpaRepository userCouponJpaRepository;

    // order
    @Autowired
    protected OrdersJpaRepository ordersJpaRepository;
    @Autowired
    protected OrderDetailJpaRepository orderDetailJpaRepository;

    // payment
    @Autowired
    protected PaymentJpaRepository paymentJpaRepository;

    // product
    @Autowired
    protected ProductJpaRepository productJpaRepository;
    @Autowired
    protected ProductStockJpaRepository productStockJpaRepository;

    // user
    @Autowired
    protected UserJpaRepository userJpaRepository;
    @Autowired
    protected UserWalletJpaRepository userWalletJpaRepository;

    @BeforeEach
    void init(){
        couponJpaRepository.deleteAllInBatch();
        couponQuantityJpaRepository.deleteAllInBatch();
        userCouponJpaRepository.deleteAllInBatch();
        ordersJpaRepository.deleteAllInBatch();
        orderDetailJpaRepository.deleteAllInBatch();
        paymentJpaRepository.deleteAllInBatch();
        productJpaRepository.deleteAllInBatch();
        productStockJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
        userWalletJpaRepository.deleteAllInBatch();
    }
}