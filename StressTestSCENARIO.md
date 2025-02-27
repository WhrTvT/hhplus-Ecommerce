# 부하테스트 시나리오(feat. APM)
---

## 개요
아래의 내용을 통해 `장애 예방`과 `장애 대응`에 대해 Araboza...
1. 장애 예방(부하테스트)
    - 테스트 API 선정
    - 테스트 시나리오
      - 테스트 플로우
        - 선착순 쿠폰 발급(`CouponIssue`)
        - 주문 결제(`doPayment`)
    - 테스트 도구 선정
    - 테스트 진행
      - 선착순 쿠폰 발급(`CouponIssue`)
        - Peak Test
      - 주문 결제(`doPayment`)
        - Load Test
    - 테스트 결과
      - 개선방안
      - 개선결과
2. 장애 대응
   - 장애 인지
   - 장애 전파
   - 장애 해결
   - 장애 회고
---

## 1. 장애 예방(부하 테스트)
- 서비스 중인 시스템이 갑자기 다운되거나 문제가 생긴다면, 개발자의 입장에서는 매우 큰일이다.
- 이러한 일을 **예방**하기 위해 부하테스트를 통해 "우리 시스템은 이정도 트래픽까지는 커버 가능해"라는 기준을 잡을 수 있다.

### 테스트 API 선정
- 선착순 쿠폰 발급(`CouponIssue`): 쿠폰 발급이 선착순이기 때문에, 쿠폰 발급 시작 시간에 일시적으로 트래픽이 몰릴 것을 가정해서 Peak Test를 진행한다.
- 주문 결제(`doPayment`): 주문 결제는 일시적 트래픽보다 지속적인 트래픽이 꾸준히 발생한다는 가정으로 Load Test를 진행한다.
---

### 테스트 시나리오
- `예상 TPS (Transaction Per Second)`, `평균/중간/최대 응답시간`, `다량의 트래픽 유입 시 동시성 이슈 발생 여부`를 확인하여 장애를 예방하는 것을 테스트 목적으로 한다.

#### 테스트 플로우
- 선정된 API의 테스트 플로우를 작성한다.

##### 선착순 쿠폰 발급(`CouponIssue`)
- 시나리오 : 발급 시간에 맞춰 1000명의 사용자가 동시에 쿠폰을 발급한다. 분산 서버 환경임을 가정(2대)하면 500 TPS를 확보해야 한다.
1. 쿠폰 발급
    - 쿠폰 발급 요청을 보내고 조건에 부합하면 쿠폰을 발급한다.
2. 쿠폰 조회
    - 쿠폰 발급 후, 1초의 간격을 두고 발급된 쿠폰이 유효하고 사용이 가능한지 조회한다. 

##### 주문 결제(`doPayment`)
- 시나리오 : 행사기간을 가정하고, 매 초마다 1000명의 사용자가 주문을 결제한다. 분산 서버 환경임을 가정(2대)하면 500 TPS를 확보해야 한다.
2. 상품 주문 내역 조회
   - 결제를 위해 상품 주문 내역을 조회한다.
2. 주문 결제
   - 상품 주문 내역 조회 후, 1초의 간격을 두고 주문ID와 주문 방법을 전달받아서 결제를 진행한다.

---

### 테스트 도구 선정
- (사용을 고려한) 테스트 도구 목록: JMeter, k6, Artillery
- 사용하기로 결정한 도구는 **k6**, 이유는 아래와 같다.
  1. 작성 스크립트
     - JMeter: XML 기반으로, XML 지식이 전무해서 사용성은 셋 중 제일 나쁘다고 생각했다.
     - **k6: 많이 사용한 JavaScript를 사용하기 때문에 무난하다고 판단했다.**
     - Artillery: yaml 형식이기 때문에 셋 중에서는 제일 작성이 편하다.
  2. 개인적 이유(?)
     - JMeter: 가장 오래된 도구인만큼 자료도 많고 아는 사람도 많지만, 본 프로젝트는 학습이 목적이기 때문에 굳이 JMeter를 배워야 하는지에 대한 고민이 있었다.
     - **k6: 알려진 것으로 따지면 k6도 JMeter에 뒤쳐지지 않는다.(오히려 더 많이 알수도..?) 또한, APM(어플리케이션 성능 모니터링)을 연동하고 결과를 분석하는 과정이 나에게 도움이 될 것 같았다.**
     - Artillery: 자체 GUI도 있고 학습 자체는 제일 쉽지 않을까 생각된다. 하지만, 현업에서 많이 쓰이나에 대한 고민과 APM 연동이 필요 없다는 장점이 오히려 나에게는 단점이 되었다.
---

### 테스트 진행
- k6로 테스트 스크립트를 작성하고, 테스트를 진행한다.

#### 선착순 쿠폰 발급(`CouponIssue`)
##### Peak Test
- 쿠폰 발급 오픈런 상황을 가정해서 1초에 500명의 가상 사용자가 쿠폰 발급을 요청하고, 1초 뒤 쿠폰 조회를 요청한다.
```javascript
import http from 'k6/http';
import {check, sleep} from 'k6';
import { SharedArray } from 'k6/data';

// k6 부하 테스트 시나리오 설정
export let options = {
    iterations: 500,
    vus: 500,
    thresholds: {
        // 95%의 요청은 500ms 미만에 응답해야 함.
        http_req_duration: ['p(95)<500'],
    },
};

export default function () {
    // 1. 쿠폰 발급 요청
    let userId = userIds[__VU - 1];
    const issueAt = formatLocalDateTime(new Date());

    let url = 'http://host.docker.internal:8080/api/commerce/coupon';
    let payload = JSON.stringify({
        couponId: 3,
        issueAt: issueAt
    });

    let params = {
        headers: {
            'Content-Type': 'application/json',
            'X-User-Id': `${userId}`
        },
    };

    let couponRes = http.patch(url, payload, params);

    // 쿠폰 발급 성공 여부 체크 (HTTP 200이면 성공으로 가정)
    const isStatus200 = check(couponRes, {'쿠폰 발급 성공': (r) => r.status === 200});

    // 응답 값을 로그로 출력
    if (isStatus200) {
        console.log(`couponRes Response Body: ${couponRes.body}`);
    } else {
        console.log(`couponRes Request failed with status: ${couponRes.status}`);
        console.log(`couponRes Response Body: ${couponRes.body}`);
    }

    let couponId = couponRes.json('data.couponId');
    if (!couponId) {
        console.error('couponId가 응답에서 추출되지 않았습니다.');
    } else {
        sleep(1);
        let myCouponRes = http.get(`http://host.docker.internal:8080/api/commerce/mycoupon/${userId}`);
        const isStatus200 = check(myCouponRes, {'쿠폰 조회 성공': (r) => r.status === 200});

        // 응답 값을 로그로 출력
        if (isStatus200) {
            console.log(`myCouponRes Response Body: ${myCouponRes.body}`);
        } else {
            console.log(`myCouponRes Request failed with status: ${myCouponRes.status}`);
            console.log(`myCouponRes Response Body: ${myCouponRes.body}`);
        }
    }

    sleep(0.5);
}

// LocalDateTime 형식으로 현재 시간을 포맷하는 함수
function formatLocalDateTime(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

// 1부터 10000까지 숫자를 생성하고, Fisher-Yates 알고리즘으로 섞은 후
// 앞의 500개 숫자를 사용합니다.
const userIds = new SharedArray('userIds', function () {
    let arr = [];
    for (let i = 1; i <= 10000; i++) {
        arr.push(i);
    }
    // Fisher-Yates shuffle
    for (let i = arr.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [arr[i], arr[j]] = [arr[j], arr[i]];
    }
    return arr.slice(0, 500);
});
```

- 주요지표
  - 서버 연결 시간 (http_req_connecting)
    - 서버 연결 평균 시간(avg)은 37.24ms로 느린 속도이지만, 중앙값은 4.09ms로 빠른 속도를 보여줍니다.
    - 이 차이는 90번째 백분위(118.09ms)와 95번째 백분위(123.05ms)처럼 일부 연결에서 상당한 지연이 발생하고 있기 때문입니다.

  - HTTP 요청 응답 시간 (http_req_duration)
    - 요청 응답 시간에 평균 24.77s가 소요되었고, 중앙값은 24.89s로 평균과 별 다른 차이없이 오랜 시간이 걸렸습니다.
    - 상위 10~5% 요청(p90/p95)은 최대 50초에 가까운 지연을 보이고 있어, 서버의 응답 처리 성능에 심각한 병목 현상이 존재함을 보여줍니다.

  - 서버 응답 대기 시간 (http_req_waiting)
    - 응답 대기 시간의 평균은 24.77s이고, 중앙값은 24.89s로 `http_req_duration`과 똑같은 시간이 소요되었습니다.
    - `http_req_waiting`은 서버가 실제로 요청을 처리하는 데 걸린 시간이지만, `http_req_duration`과 거의 동일한 값을 보인다는 것은 서버 내부 처리 시간이 전체 지연의 대부분을 차지하고 있음을 나타냅니다.

- 요약
  - 쿠폰 발급 요청의 성공률은 약 89%로, 500회 중 446회가 성공했고 54회가 실패했습니다.
  - 평균 25초의 응답 시간(`http_req_duration`)은 매우 긴 지연을 의미하며, p90과 p95에서는 41~45초, 최대 50초까지 소요되고 있습니다.
  - 이는 서버가 부하로 인해 요청을 처리하는 데 큰 병목이 있음을 시사합니다.
  - 결과적으로 서버에 순간적인 부하가 가해졌고, Peak Test를 성공적으로 수행했음을 나타냅니다.
  
##### Load Test
- 1초동안 가상 사용자를 5명 증가시켜서 웜업을 진행하고, 그 후 5초마다 가상 사용자를 100/200/300/400명까지 증가시킨다.
- 이후, 10초 동안 600명까지 증가시켰다가 다음 10초 동안 500명까지 줄이고, 마지막 5초 동안 모든 가상 사용자를 종료시킨다.
- **본 테스트는 Peak 테스트에서 문제가된 DeadLock/Connection Pool 문제를 해결하고 테스트를 진행했다.**
```javascript
import http from 'k6/http';
import {check, sleep} from 'k6';
import {randomIntBetween} from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

// k6 부하 테스트 시나리오 설정
export let options = {
    stages: [
        {duration: '1s', target: 5},
        { duration: '5s', target: 100 },
        { duration: '5s', target: 200 },
        { duration: '5s', target: 300 },
        { duration: '5s', target: 400 },
        { duration: '10s', target: 600 },
        { duration: '10s', target: 500 },
        { duration: '5s', target: 0 }
    ],
};

export default function () {
    // 1. 쿠폰 발급 요청
    let userId = randomIntBetween(1, 10000);
    const issueAt = formatLocalDateTime(new Date());

    let url = 'http://host.docker.internal:8080/api/commerce/coupon';
    let payload = JSON.stringify({
        couponId: 3,
        issueAt: issueAt
    });

    let params = {
        headers: {
            'Content-Type': 'application/json',
            'X-User-Id': `${userId}`
        },
    };

    let couponRes = http.patch(url, payload, params);

    // 쿠폰 발급 성공 여부 체크 (HTTP 200이면 성공으로 가정)
    const isStatus200 = check(couponRes, {'쿠폰 발급 성공': (r) => r.status === 200});

    // 응답 값을 로그로 출력
    if (isStatus200) {
        console.log(`couponRes Response Body: ${couponRes.body}`);
    } else {
        console.log(`couponRes Request failed with status: ${couponRes.status}`);
        console.log(`couponRes Response Body: ${couponRes.body}`);
    }

    let couponId = couponRes.json('data.couponId');
    if (!couponId) {
        console.error('couponId가 응답에서 추출되지 않았습니다.');
    } else {
        sleep(1);
        let myCouponRes = http.get(`http://host.docker.internal:8080/api/commerce/mycoupon/${userId}`);
        const isStatus200 = check(myCouponRes, {'쿠폰 조회 성공': (r) => r.status === 200});

        // 응답 값을 로그로 출력
        if (isStatus200) {
            console.log(`myCouponRes Response Body: ${myCouponRes.body}`);
        } else {
            console.log(`myCouponRes Request failed with status: ${myCouponRes.status}`);
            console.log(`myCouponRes Response Body: ${myCouponRes.body}`);
        }
    }

    sleep(0.5);
}

// LocalDateTime 형식으로 현재 시간을 포맷하는 함수
function formatLocalDateTime(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}
```

- 주요지표
    - 서버 연결 시간 (http_req_connecting)
        - 서버 연결 평균 시간은 2.03ms로 빠른 속도로 연결된다., 중앙값은 0s로 즉시 연결되었다.

- HTTP 요청 응답 시간 (http_req_duration)
    - 요청 응답 시간에 평균 19.44s가 소요되었고, 중앙값은 18.96s로 평균과 별 다른 차이없는 시간이 걸렸습니다.
    - 상위 10~5% 요청(p90/p95)은 최대 45초에 가까운 지연을 보이고 있어, `Peak Test`와 마찬가지의 결과를 보여줍니다.

- 서버 응답 대기 시간 (http_req_waiting)
    - 응답 대기 시간의 평균은 19.44s이고, 중앙값은 18.96s로 `http_req_duration`과 똑같은 시간이 소요되었습니다.
    - `http_req_waiting`은 서버가 실제로 요청을 처리하는 데 걸린 시간이지만, `http_req_duration`과 거의 동일한 값을 보인다는 것은 서버 내부 처리 시간이 전체 지연의 대부분을 차지하고 있음을 나타냅니다.

- 요약
    - 쿠폰 발급 요청의 성공률은 약 90%로, 700회 중 635회가 성공했고 70회가 실패했습니다.
    - 응답 시간(`http_req_duration`)의 최대(45sec)와 최소(21sec) 시간의 간격이 큰 이유는 시간이 지난수록 많은 유저가 접근하면서 요청 응답 시간이 지연되는 것을 의미합니다.
    - 결과적으로 서버에 지속적인 부하가 가해졌고, Load Test를 성공적으로 수행했음을 나타냅니다.

---

### 테스트 결과
- 테스트 결과 분석하고, 개선방안을 찾아보고 적용한다.

#### 개선방안
- 선착순 쿠폰 발급(`CouponIssue`)
  - 선착순 쿠폰 발급에 대한 Peak Test의 성공률이 89%밖에 되지 않아, 성공률을 높일 방법을 찾아 적용해본다.
    1. DeadLock(DB 비관락)
       - MySQL 쿼리(`show engine innodb status;`)로 InnoDB의 현재 상태를 출력하여 데드락 현상을 확인했습니다.
       - 쿠폰 발급에 대한 트래픽이 한번에 몰리면서 쿠폰 개수 조회의 비관락(`PESSIMISTIC_WRITE`)에서 데드락이 발생했습니다.
       - 이를 해결하기 위해 분산락(Redis)으로 쿠폰 개수를 제어할 수 있지만,<br>
         현재 상황에서 적용하기는 어렵기 때문에 '쿠폰 발급이 쿠폰 개수를 초과하지 않는다'라고 가정하고 비관락을 해제했습니다.
       
    2. DB Connection Pool
       - `application.yml`에서 `maximum-pool-size` 옵션을 통해 DB Connection Pool Size를 조절할 수 있습니다.
       - 현재 `10`로 설정되어 있으며, 테스트를 위해 일시적으로 `100`으로 증가했습니다.

#### 개선결과
- 선착순 쿠폰 발급(`CouponIssue`)
  - 개선방안 적용 후, 선착순 쿠폰 발급에 대한 Peak Test의 성공률이 99%로 증가했다.
  
  - 주요지표
    - 서버 연결 시간 (http_req_connecting)
        - 서버 연결 평균 시간 : 118.57ms / 중앙값 : 15.09ms
        - 개선 전과 비교하면 느려졌으나 큰 폭의 변동은 아니라고 판단됩니다.
      
    - HTTP 요청 응답 시간 (http_req_duration)
        - 요청 응답 시간 평균 시간 : 24.35s / 중앙값 : 23.91s
        - 개선 전과 비교하면 미세하게 빨라졌습니다. 

    - 서버 응답 대기 시간 (http_req_waiting)
      - 응답 대기 평균 시간 : 24.35s / 중앙값 : 23.89s
      - 개선 전과 비교하면 미세하게 빨라졌습니다.

---

## 2. 장애 대응
- 장애 대응은 이미 장애가 발생한 상황에서, 문제를 해결하기 위한 후속 조치이다.
### 장애 인지
- 장애가 발생하면 어떤 방식으로든 **인지**하는 과정을 거친다.
  1. **시스템에서 장애를 탐지해서 관련자에게 Alert(Slack, pager duty, squad cast 등)**
  2. 운영 측에서 실시간 모니터링을 통해 발견 (공공기관 24시간 관제 시스템...)
  3. CS를 통해 고객에게 장애 소식 전달받기 (제일 최악이다)
  
### 장애 전파
- 장애를 인지하면 장애에 대한 내용을 팀과 관련자에게 **전파**한다.
- 필요에 따라 홈페이지나 기타 방법으로 고객에게도 **전파**한다.

### 장애 해결
- 제일 급하다. 일단 해결해야 나중을 생각할 수 있기 때문에 시간을 쏟아 해결한다.
- 장애에는 여러 종류가 있는만큼 해결 방법도 많다.
  1. 서버 부하 → 재배포
  2. 추가된 feature 장애 → 롤백
  3. 새로운 버그 → 핫픽스
  ...
- 1, 2, 3 이후에 필요하다면 데이터 보정과 코드 수정 작업을 진행한다.
### 장애 회고
- 이번 주차의 핵심이다..
- 장애가 해결되면 관련 정보를 공유하고 임팩트(영향도)와 재발 방지 대책 등을 논의한다.
- 보고서의 내용은 보통 아래의 순서를 따른다.
  1. 장애 공유
  2. 임팩트(영향도) 파악
  3. 장애 대응 타임라인
     - 08:00 장애인지
     - 08:05 롤백
     - ...
     - 08:40 데이터 보정
     - 09:00 수정배포(장애마감)
  4. 장애 해결방안
  5. **재발방지 논의**
     - 근복적 원인 파악
     - (재발방지를 위한) 근본적 원인 해결책 제시
       1. 단기 목표
       2. 장기 목표

- 재발방지 논의에서 중요한 것은 `근본적 원인`과 `(근본적 원인) 해결책`이다.
- 근본적 원인을 찾기 위해 **5whys** 기법을 적용할 수 있다.
  - 5whys는 형식일 뿐이며, 3whys가 될 수도 있고 6whys가 될 수도 있다.
  - 이를 통해 표면적인 문제를 더 깊이 추적해서 본질적인 원인을 찾아 낼 수 있다.
  
- 근본적 원인을 찾았다면 해결책을 제시해야 한다.
  - 원인 파악보다, 해결책은 좀 더 쉽게 제시할 수 있다.<br>
    (메모리 누수로 인해 서버에서 OOM가 발생했다면, 비용을 투자해서 서버를 증설할 수 있다.)
  - 위의 경우, 서버 증설은 **단기 목표**가 될 것 이고, **장기 목표**는 트래픽 제어를 위한 무언가를 하는 것이다.  