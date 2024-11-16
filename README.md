## 프로젝트 소개

이 서비스는 스트리밍 플랫폼 내에서 **영상 제작자에게 특화된 정산 및 통계 데이터를 제공하는 서비스**입니다.<br>
영상 제작자는 이 서비스를 통해 **업로드한 영상의 일별, 주별, 월별 수익과 통계 파악** 및 **자신의 콘텐츠 성과를 모니터링하고 파악**할 수 있습니다.<br>

## 주요 기능

### 📡 스트리밍

> **업로드된 영상 재생**

- 대용량 트래픽이 예상되는 영상 재생 API의 안정적이고 효율적인 처리를 위해 Redis에 영상별 조회수 캐싱
- 캐싱된 조회수는 스케줄러를 통해 주기적으로 Bulk Insert 처리하여 조회수 업데이트 최적화

> **30초 내 동일 IP, 계정 접속 시 어뷰징 방지**

- Redis TTL 설정을 통해 30초 이내 동일 IP, 계정 접속 시 어뷰징으로 판단
- Redisson 분산 락 적용을 통해 어뷰징 판단 시 발생할 수 있는 동시성 문제 해결 및 경쟁 상태 방지

> **영상 재생 종료**

- 대용량 트래픽이 예상되는 영상 종료 API의 안정적인 처리를 위해 Redis를 활용하여 시청 로그 기록
- @Async 어노테이션을 사용한 비동기 처리를 통해 클라이언트 응답 지연 최소화

### 🧮 영상 수익 및 통계

> **일일 스트리밍이 발생한 영상에 대해 통계 및 정산**

- SpringBatch 파티셔닝, DB 인덱싱, JDBC Batch Insert 단계별 개선을 통해 배치 처리 최적화

> **영상별 일별, 주별, 월별 수익 및 통계 조회**

- 일간, 주간, 월간 조회수 TOP5 동영상 조회
- 일간, 주간, 월간 재생 시간 TOP5 동영상 조회
- 일간, 주간, 월간 총 정산금액, 영상별 정산금액, 광고별 정산금액 조회

## 기술 스택

- **Language** : ![Java](https://img.shields.io/badge/Java17-%23ED8B00.svg?style=square&logo=openjdk&logoColor=white) <br>
- **Framework** : <img src = "https://img.shields.io/badge/Springboot 3.3.5-6DB33F?&logo=springboot&logoColor=white"> <img src = "https://img.shields.io/badge/Spring Batch 5.1.2 -6DB33F?&logo=Spring&logoColor=white"> ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=square&logo=Spring&logoColor=white) <br>
- **Build** : ![Gradle](https://img.shields.io/badge/Gradle%208-02303A.svg?style=square&logo=Gradle&logoColor=white)
- **Database** : <img src = "https://img.shields.io/badge/MySQL 8-4479A1?&logo=MySQL&logoColor=white"> <img src = "https://img.shields.io/badge/Redis-FF4438?&logo=redis&logoColor=white">
- **DevOps** :<img src = "https://img.shields.io/badge/Docker-2496ED?&logo=docker&logoColor=white">
- **IDE** : <img src = "https://img.shields.io/badge/Intellij Idea-000000?&logo=intellijidea&logoColor=white">
- **Version Control** : <img src = "https://img.shields.io/badge/Git-F05032?&logo=git&logoColor=white"> <img src = "https://img.shields.io/badge/Github-181717?&logo=github&logoColor=white">

## 시스템 아키텍처

## 트러블 슈팅

[**스트리밍 정산 및 통계 배치 단계별 최적화**](https://github.com/younghyun-j/streaming-settlement)

- SpringBatch 파티셔닝 적용으로 배치 처리 속도 개선
- DB 인덱싱을 통한 쿼리 성능 향상
- JDBC Batch Insert를 사용하여 DB 쓰기 성능 최적화

[**스트리밍 어뷰징 캐싱 동시성 문제**](https://github.com/younghyun-j/streaming-settlement)

- Redis TTL 설정으로 어뷰징 판단 데이터 관리
- Redisson 분산 락 적용으로 동시성 문제 해결

[**스트리밍 조회수 개별 업데이트 시, 데이터베이스 부하 문제**](https://github.com/younghyun-j/streaming-settlement)

- Redis에 조회수 캐싱 후 주기적 Bulk Insert 처리로 데이터베이스 부하 최소화

[**스트리밍 시청 종료 로그 처리 최적화**](https://github.com/younghyun-j/streaming-settlement)

- Redis에 시청 로그 캐싱 후 배치 처리하여 API 요청 및 트래픽 효율화
- @Async 어노테이션을 사용한 비동기 처리를 통해 클라이언트 응답 지연 최소화

## ERD
