package kr.hhplus.be.server.infrastructure.product;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.product.ProductCustomRepository;
import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.domain.order.QOrderDetail.orderDetail;
import static kr.hhplus.be.server.domain.order.QOrders.orders;
import static kr.hhplus.be.server.domain.payment.QPayment.payment;
import static kr.hhplus.be.server.domain.product.QProduct.product;
import static kr.hhplus.be.server.domain.product.QProductStock.productStock;

@RequiredArgsConstructor
@Repository
public class ProductCustomImplRepository implements ProductCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProductWithProductStockDTO> findProductWithStockByTop(long top, long day) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                ProductWithProductStockDTO.class,
                                product.productId,
                                product.name,
                                product.detail,
                                product.price,
                                orderDetail.selectQuantity.sum()
                        )
                )
                .from(product)
                .innerJoin(productStock).on(product.productId.eq(productStock.productId))
                .innerJoin(orderDetail).on(product.productId.eq(orderDetail.productId))
                .innerJoin(orders).on(orderDetail.orderId.eq(orders.orderId))
                .innerJoin(payment).on(orders.orderId.eq(payment.orderId))
                .where(
                        payment.status.eq("SUCCESS")
                        .and(payment.paymentAt.goe(LocalDateTime.now().minusDays(day)))
                        .and(productStock.quantity.isNotNull())
                )
                .groupBy(product.productId)
                .orderBy(orderDetail.selectQuantity.sum().desc())
                .limit(top)
                .fetch();
    }
}
