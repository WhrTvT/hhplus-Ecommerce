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