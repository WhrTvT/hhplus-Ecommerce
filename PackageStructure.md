├─main
│  ├─java
│  │  └─kr
│  │      └─hhplus
│  │          └─be
│  │              └─server
│  │                  ├─application             -> UseCase 계층(UseCase / DTO)
│  │                  │  ├─in                           --> Command DTO
│  │                  │  └─out                          --> Info DTO
│  │                  ├─common                  -> 패키지 공통 기능 관리
│  │                  │  └─exception                    --> 비즈니스 로직의 예외 처리
│  │                  ├─domain                  -> Domain 계층(Business / Entity)
│  │                  │  ├─coupon                       --> 쿠폰 서비스
│  │                  │  ├─order                        --> 주문 서비스
│  │                  │  │  └─response                          ---> 주문관련 DTO
│  │                  │  ├─payment                      --> 결제 서비스
│  │                  │  ├─product                      --> 상품 서비스
│  │                  │  │  └─response                          ---> 상품관련 DTO
│  │                  │  └─user                         --> 유저 서비스
│  │                  ├─infrastructure          -> DATA 계층(JPA, Impl)
│  │                  │  ├─config                       --> infrastructure 계층 설정 관련
│  │                  │  │  ├─jpa                               -> jpa 설정
│  │                  │  │  └─queryDSL                          -> queryDSL 설정
│  │                  │  ├─coupon                       --> 쿠폰 인프라
│  │                  │  ├─orders                       --> 주문 인프라
│  │                  │  ├─payment                      --> 결제 인프라
│  │                  │  ├─product                      --> 상품 인프라
│  │                  │  └─user                         --> 유저 인프라
│  │                  └─interfaces              -> API 계층(presentation / Controller / DTO)
│  │                      ├─config                      --> interfaces 계층 설정 관련
│  │                      │  ├─filter                           -> filter 설정
│  │                      │  ├─interceptor                      -> interceptor 설정
│  │                      │  └─swagger                          -> swagger 설정
│  │                      ├─mock                        --> mockAPI 관련 파일
│  │                      │  ├─request                          -> mockAPI Request DTO
│  │                      │  └─response                         -> mockAPI Response DTO
│  │                      ├─request                     --> Request DTO
│  │                      └─response                    --> Response DTO