## 이커머스 서비스 BE개발 프로젝트

## 시작하기

### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행 팔수

```bash
docker-compose up -d
```
---

## 이커머스 시스템 구축

### 참고자료
1. MileStone : [projects] (https://github.com/users/WhrTvT/projects/2)
2. API Specs : [Swagger] (https://github.com/WhrTvT/hhplus-Ecommerce/issues/12), [postman] (https://github.com/WhrTvT/hhplus-Ecommerce/issues/6)
3. ERD : https://github.com/WhrTvT/hhplus-Ecommerce/issues/5
   ![image](https://github.com/user-attachments/assets/cf7c4366-e4c9-4281-8e88-09e30c2b415c)
---

### Swagger

#### 결과물
![image](https://github.com/user-attachments/assets/66bd9f36-ff79-423f-9a58-e4f1bdf49d5c)

#### Config [Commit_Link](https://github.com/WhrTvT/hhplus-Ecommerce/pull/25/commits/aa9a67f45944be29a96c2f65b2479faae8f9f36c)
- customInfo를 통해 API description 생성.
- customServer를 통해 localhost URL 지정.


#### annotation
1. `@Tag` : 클래스 어노테이션
    - Summary : 제목
    - description : 클래스에 대한 설명을 작성합니다.
2. `@Operation` : 메소드 어노테이션
    - Summary : 제목
    - description : 클래스별 API에 대한 설명을 작성합니다.
3. `@Parameter` : 메소드 어노테이션
    - name : 파라미터 NAME
    - description : 파라미터에 대한 설명을 작성합니다.
---

### DataBase Concurrency

#### 결과물
[동시성제어 보고서](https://github.com/WhrTvT/hhplus-Ecommerce/blob/main/CONCURRENCY.md)

---
### Cache

#### 결과물
[캐싱전략 보고서](https://github.com/WhrTvT/hhplus-Ecommerce/blob/main/CACHE.md)

---
### MSA 분산시스템

#### 결과물
[분산 시스템 설계 보고서](https://github.com/WhrTvT/hhplus-Ecommerce/blob/main/MSA.md)

---
### 부하테스트 및 장애대응

#### 결과물
[StressTestSCENARIO.md](https://github.com/WhrTvT/hhplus-Ecommerce/blob/step19/StressTestSCENARIO.md)
[장애대응 문서](https://github.com/WhrTvT/hhplus-Ecommerce/blob/step19/StressTestSCENARIO.md#2-%EC%9E%A5%EC%95%A0-%EB%8C%80%EC%9D%91)
