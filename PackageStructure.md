─src
├─main
│  ├─java
│  │  └─kr
│  │      └─hhplus
│  │          └─be
│  │              └─server
│  │                  ├─common              ---> 패키지 공통 기능 관리
│  │                  │  └─exception                --> 비즈니스 로직의 예외 처리
│  │                  ├─domain              ---> Domain 계층(Business / Entity)
│  │                  │  ├─config                   --> Domain 계층 설정 관리
│  │                  │  ├─coupon                   --> 쿠폰 관련 서비스
│  │                  │  ├─order                    --> 주문 관련 서비스
│  │                  │  ├─payment                  --> 결제 관련 서비스
│  │                  │  ├─product                  --> 상품 관련 서비스
│  │                  │  └─user                     --> 유저 관련 서비스
│  │                  ├─infrastructure      ---> DATA 계층(JPA, Impl)
│  │                  │  └─config                   --> infrastructure 계층 설정 관리
│  │                  │      └─jpa                  --> jpa 설정 관련 config
│  │                  └─interfaces          ---> API 계층(presentation / Controller / DTO)
│  │                      ├─config                  --> interfaces 계층 설정 관리
│  │                      ├─request                 --> Request 관련 DTO 객체 관리
│  │                      └─response                --> Response 관련 DTO 객체 관리