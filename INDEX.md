# 이커머스 서비스의 인덱스 추가와 성능 개선 검증
---

## 개요
아래의 내용을 수행하여 서비스 실행 속도를 단축하고 트래픽 부하에 대비한다.
1. 이커머스 서비스에서 수행하는 쿼리 수집.
2. 인덱스 추가가 필요하다고 판단되는 쿼리에 인덱스 추가.
3. 인덱스 추가 전/후의 성능을 측정하여 검증.
---

## Index
- Windows 자료구조의 색인 개념과 유사하다.
- 데이터의 위치를 빠르게 찾아주는 역할로, 테이블의 동작속도(조회)를 높여준다.
- READ는 빨라지지만 CUD는 느려지기 때문에 UPDATE가 잘 이루어지지 않는 컬럼에 적용하는 것이 바람직하다.

### 단일 컬럼 인덱스
- 카디널리티(Cardinality): 단일 컬럼 인덱스를 생성할 때는 해당 컬럼의 카디널리티가 높은 것을 선택해야 함.
카디널리티가 높은 컬럼은 중복된 값이 적어 인덱스를 통해 데이터의 대부분을 효율적으로 필터링할 수 있음.
  - 카디널리티가 높은 컬럼 예시: 주민등록번호, 계좌번호 등등...
  - 카디널리티가 낮은 컬럼 예시: 성별, 학년 등등...

### 복합 컬럼 인덱스
- 카디널리티 순서: 여러 컬럼으로 인덱스를 구성할 때는 카디널리티가 높은 순서에서 낮은 순서로 인덱스를 구성하는 것이 좋음.
인덱스가 효율적으로 많은 데이터를 걸러낼 수 있도록 하기 위함.
  - Ex) CREATE INDEX idx_example ON table_name (high_cardinality_column, low_cardinality_column);

### 주의사항
- 복합 컬럼 인덱스 사용시 반드시 인덱스의 첫 번째 컬럼은 조회 조건에 포함 되어야 인덱스가 적용됨. 중간이나 마지막 컬럼이 누락된 경우에는 인덱스를 사용할 수 있지만, 첫 번째 컬럼이 누락된 경우에는 인덱스를 사용할 수 없음.
    - Ex) 인덱스가 idx_example (column1, column2, column3)로 구성된 경우, column1은 반드시 조회 조건에 포함되어야 함.
- `BETWEEN`, `LIKE`, `<`, `>` 등 범위 조건을 사용하면 해당 컬럼까지만 인덱스가 사용되고, 그 이후의 컬럼은 인덱스를 사용하지 않음.
    - Ex) WHERE `column1`, `column2`, `column3` 가 인덱스로 잡혀있고 `column1 = value1 AND column3 = value3 AND column2 > value2` 인 경우, column3은 인덱스가 사용되지 않음.
    - `=`, `in`의 경우 다음 컬럼도 인덱스 사용
- AND 연산자: 각 조건이 읽어야 할 행(row)의 수를 줄이는 역할을 하므로 인덱스 효율을 높임.
- OR 연산자: 비교해야 할 행의 수를 늘리기 때문에 풀 테이블 스캔이 발생할 확률이 높음.
- where salary * 10 > 150000;는 인덱스를 못타지만, where salary > 150000 / 10; 은 인덱스를 사용.
---

## 이커머스 서비스 적용
이커머스 서비스에 적용하기 위한 사전 작업을 진행한다.
1. 이커머스 서비스의 주요 쿼리 확인.
2. 주요 쿼리에 인덱스를 적용할 대상 분류.

### 주요 쿼리
1. 쿠폰재고 조회 : Optional<CouponQuantity> findCouponQuantityById(long couponId);
2. 사용자 쿠폰 전체 조회 : Page<UserCoupon> findAllByUserId(long userId, Pageable pageable);
3. 사용자 쿠폰 조회 : Optional<UserCoupon> findByUserIdAndCouponId(long userId, long couponId);
4. 주문 상세 전체 조회 : List<OrderDetail> findAllByOrderId(long orderId);
5. 주문 조회 : Optional<Orders> findByIdWithLock(Long orderId);
6. 주문 결제 상태 Bool : boolean findPaymentByOrderIdAndStatusWithLock(long orderId, String status);
7. 주문 결제 조회 : Payment findByOrderId(long orderId);
8. 인기 상품 전체 조회 : List<ProductWithProductStockDTO> findProductWithStockByTop(long top, long day)
9. 상품 전체 조회 : Page<ProductWithProductStockDTO> findProductWithStock(Pageable pageable);
10. 상품 조회 : Optional<ProductStock> findByProductIdWithLock(long productId);
11. 유저 지갑 조회 : Optional<UserWallet> findByUserIdWithLock(long userId);

### 인덱스 고려 대상
1. 사용자 쿠폰 전체 조회 : Page<UserCoupon> findAllByUserId(long userId, Pageable pageable);
   - select uc.* from user_coupon uc where uc.user_id=1 LIMIT 20;
     <br>: 20row / 0.00sec
   - select count(uc.user_coupon_id) from user_coupon uc where uc.user_id=1;
     <br>: 1row / 0.02sec
2. 주문 상세 전체 조회 : List<OrderDetail> findAllByOrderId(long orderId);
   - select od.* from order_detail od where od.order_id=1
     <br>: 50001row / 0.08sec
3. 주문 결제 상태 Bool : boolean findPaymentByOrderIdAndStatusWithLock(long orderId, String status);
   - select case when count(p.payment_id)>0 then 1 else 0 end from payment p where p.order_id=1 and p.status='SUCCESS';
     <br>: 1row / 0.00sec
4. 인기 상품 전체 조회 : List<ProductWithProductStockDTO> findProductWithStockByTop(long top, long day)
   - select p.product_id,p.name,p.detail,p.price,sum(od.select_quantity) 
     from product p
     join product_stock ps on p.product_id=ps.product_id
     join order_detail od on p.product_id=od.product_id
     join orders o on od.order_id=o.order_id
     join payment p2 on o.order_id=p2.order_id
     where
         p2.status='SUCCESS'
         and p2.payment_at>='2025-02-01'
         and ps.quantity is not null
     group by p.product_id
     order by sum(od.select_quantity) DESC
     limit 5;
     <br>: 5row / 0.37sec
5. 상품 전체 조회 : Page<ProductWithProductStockDTO> findProductWithStock(Pageable pageable);
   - select p.product_id,p.name,p.detail,p.price,ps.quantity from product p join product_stock ps on p.product_id=ps.product_id LIMIT 20;
     <br>: 20row / 0.00sec
   - select count(p.product_id) from product p join product_stock ps on p.product_id=ps.product_id;
     <br>: 1row / 0.19sec
---

## 성능 개선과 검증(기준 : 10만 ROW)
1. 사용자 쿠폰 전체 조회
    - 인덱스 설정 여부 : X
    - 성능 개선 : LIMIT을 통한 페이징 처리와 기본키(PK)로 이미 인덱스를 타기 때문에 작업 불필요.
   
2. 주문 상세 전체 조회
    - 인덱스 설정 여부 : X
    - 성능 개선 : order_id가 이미 후보키(Candidate Key)로 설정되어 있고, 인덱스를 타고 있기 때문에 작업 불필요.
   
3. 주문 결제 상태 Bool
    - 인덱스 설정 여부 : X
    - 성능 개선 : 1(TRUE)과 0(FALSE)를 리턴하는 쿼리로, 생성 ROW가 적기 때문에 작업 불필요.
   
4. 인기 상품 전체 조회
    - 인덱스 설정 여부 : O
    - 성능 개선 : 여러 테이블의 JOIN으로 인해 Payment(결제) 테이블이 풀 스캔되고 있으므로, 작업 필요.
    - 쿼리 구문 :
       ![4](https://github.com/user-attachments/assets/e985ce8f-5ee3-472b-a51f-c05d3d2d5d6d)

    - 인덱싱 전
      - Explain
        - type: ALL
        - rows: 99,704
        - Extra: `Using where; Using temporary; Using filesort`
          ![4-explain](https://github.com/user-attachments/assets/d5dd6fdb-7c22-4583-8c5b-9260b98d9b06)
          - `Using where`: MySQL 서버가 스토리지 엔진에서 값을 가져온 뒤 행을 필터링 한다는 것을 의미
          - `Using temporary`: MySQL이 쿼리 결과를 정렬하기 위해 임시 테이블을 사용한다는 의미
          - `Using filesort`: MySQL이 결과의 순서를 맞추기 위해 인덱스 순서로 테이블을 읽는 것이아니라 외부 정렬을 사용해야 한다는 것을 의미
    - 인덱싱 후
      - Explain
        - type: range
        - rows: 144
        - Extra: `Using where; Using Index; Using temporary; Using filesort`
          ![44-explain](https://github.com/user-attachments/assets/1c9e6a56-e84a-4d0e-8358-5144f2fcf377)
          - `Using Index`: 커버링 인덱스를 사용 중
          - 여전히 행 필터링과 쿼리 결과 정렬을 위해 Extra에 표기되지만, 커버링 인덱스 사용이 추가되었음.

    - 성능 검증
      - 동일쿼리의 실행 속도가 0.37sec → 0.27sec로, 0.1sec 개선
        ![44](https://github.com/user-attachments/assets/0a417626-ac31-495c-b857-88c6683e9163)

    - 개선 방향
        - Payment 테이블에 복합 컬럼 인덱스(status, payment_at, order_id)를 추가함.
        <br>이유: 조건에서 사용하는 컬럼(status, payment_at)과 JOIN에서 사용하는 컬럼(order_id)을 한 인덱스로 묶어서, 조인할 때 커버링 인덱스를 활용하도록 하기 위함.
5. 상품 전체 조회
    - 인덱스 설정 여부 : X
    - 성능 개선 : LIMIT을 통한 페이징 처리를 하고 있으나, Count(상품 전체 개수) 쿼리에서 JOIN으로 인해 Product 테이블이 풀 스캔이 되기 때문에 작업이 고려됨.
                 하지만, JPA에서 생성된 쿼리인 점과 이미 커버링 인덱스를 사용 중인 점을 확인하여 작업 진행하지 않음.
    - 쿼리 구문 :
      ![5-2](https://github.com/user-attachments/assets/ff44be32-a6da-4249-b9e1-53341f789ea3)

    - Explain
      - type: index
      - rows: 99,498
      - Extra: `Using Index` - 커버링 인덱스를 사용 중.
        ![5-2-explain](https://github.com/user-attachments/assets/3724595b-c76e-4616-a837-6ccb35220649)
