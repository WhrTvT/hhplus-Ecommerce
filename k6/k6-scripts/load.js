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