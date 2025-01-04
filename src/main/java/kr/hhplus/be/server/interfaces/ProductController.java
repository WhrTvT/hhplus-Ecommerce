package kr.hhplus.be.server.interfaces;

import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.interfaces.response.ProductResponse;
import kr.hhplus.be.server.interfaces.response.TopProductResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
public class ProductController {
//    private static final Logger log = LoggerFactory.getLogger(UserWalletController.class);
//    private final ProductService productService;

    //상품 조회
    @GetMapping("/product")
    public ResponseEntity<Page<ProductResponse>> Products(
            @RequestParam(value = "page",defaultValue = "1")int page,
            @RequestParam(value = "size",defaultValue = "5")int size,
            @RequestParam(value = "sort",defaultValue = "updatedAt")String sort,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        // 정렬 방향 설정 (default는 DESC)
        Sort.Direction dic = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(dic, sort));
//        Page<ProductResponse> products = productService.getProducts(pageable);
//        log.info("Products : {}", products);

//        return ResponseEntity.ok(ProductResponse.from(products));
        return ResponseEntity.ok(ProductResponse.mock(pageable));
    }

    // 상위 상품 조회
    @GetMapping("/top-product")
    public ResponseEntity<TopProductResponse> TopProducts(
            @RequestParam(value = "top",defaultValue = "5")int top,
            @RequestParam(value = "day",defaultValue = "3")int day
    ){
//        Product products = productService.getTopProducts(top, day);
//        log.info("Products : {}", products);

//        return ResponseEntity.ok(TopProductResponse.from(top, day));
        return ResponseEntity.ok(TopProductResponse.mock(top, day));
    }
}
