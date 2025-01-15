package kr.hhplus.be.server.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.ProductUseCase;
import kr.hhplus.be.server.application.out.ProductInfo;
import kr.hhplus.be.server.common.exception.ApiResponse;
import kr.hhplus.be.server.interfaces.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product API", description = "상품 목록과 재고를 관리합니다.")
@RestController
@RequestMapping("/api/commerce")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductUseCase productUseCase;

    //상품 조회
    @Operation(summary = "상품 조회", description = "Param으로 받은 정보로 상품 목록을 생성합니다.")
    @Parameter(name = "page", description = "목록 페이지네이션")
    @Parameter(name = "size", description = "목록당 객체 출력 사이즈")
    @Parameter(name = "sort", description = "소팅 기준")
    @Parameter(name = "direction", description = "내림/오름차순")
    @GetMapping("/products")
    public ApiResponse<Page<ProductResponse>> Products(
             Pageable pageable
    ) {
        Page<ProductInfo> products = productUseCase.getProducts(pageable);
        return ApiResponse.success(products.map(ProductResponse::from));
    }

    // 상위 상품 조회
    @Operation(summary = "상위 상품 조회", description = "Param으로 받은 정보로 상위 상품 목록을 생성합니다.")
    @Parameter(name = "top", description = "상위 상품 개수")
    @Parameter(name = "day", description = "기준 날짜")
    @GetMapping("/top-product")
    public ApiResponse<List<ProductResponse>> TopProducts(
            @RequestParam(value = "top",defaultValue = "5")int top,
            @RequestParam(value = "day",defaultValue = "3")int day
    ){
        List<ProductInfo> topProducts = productUseCase.getTopProducts(top, day);
        return ApiResponse.success(topProducts.stream().map(ProductResponse::from).toList());
    }
}
