package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.response.ProductWithProductStockDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCustomRepository productCustomRepository;

    public Page<ProductWithProductStockDTO> findProducts(Pageable pageable) {
        return productRepository.findProductWithStockLock(pageable);
    }

    // 스케줄러로 요청된 데이터 외에 다른 요청 건도 캐시에 저장하기 위해 여기서 Cacheable
    // TODO - 매개변수 top, day를 Cacheable의 key로 주면 "Null key returned for cache operation" 에러발생..
    @Cacheable(value = "topProducts", key = "#p0", unless = "#result == null")
    public List<ProductWithProductStockDTO> findTopProducts(long top, long day) {
        return productCustomRepository.findProductWithStockByTop(top, day);
    }

    // 5분마다 스케줄러 실행
    @Scheduled(cron = "0 */5 * * * ?", zone = "Asia/Seoul")
    @CachePut(value = "topProducts", key = "#p0", condition = "#p0 != null")
    public void scheduleMethod() {
        List<ProductWithProductStockDTO> cache = findTopProducts(5, 3);
        log.info("findTopProducts cache: {}", cache);
    }
}
