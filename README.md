## 📢 프로젝트 소개
이 서비스는 스트리밍 플랫폼 내에서 **영상 제작자에게 특화된 정산 및 통계 데이터를 제공하는 서비스**입니다.<br>
영상 제작자는 이 서비스를 통해 **업로드한 영상의 일간, 주간, 월간 수익과 통계 파악** 및 **자신의 콘텐츠 성과를 모니터링하고 파악**할 수 있습니다.<br>
<br>

## ✨ 프로젝트 기간 및 주요 기능
### 기간
> **2024.10 ~ 2024.11 (1개월)**

### 주요 기능
<table>
  <tr>
    <th>📡 스트리밍</th>
    <th>🧮 영상 수익 및 통계</th>
  </tr>
  <tr>
    <td>영상 재생</td>
    <td>일일 스트리밍이 발생한 영상에 대해 통계 및 정산</td>
  </tr>
  <tr>
    <td>30초 내 동일 IP, 계정 접속 시 어뷰징 방지</td>
    <td>영상별 일간, 주간, 월간 수익 조회</td>
  </tr>
  <tr>
    <td>영상 재생 종료</td>
    <td>일간, 주간, 월간 Top 5 동영상 조회</td>
  </tr>
</table>
<br>

## 📌 프로젝트 주요 경험

### 1. 대용량 시청 기록 통계 및 정산 배치 성능 개선
> Spring Batch 파티셔닝, DB 인덱싱, JDBC Batch Insert 단계별 개선을 통해 배치 처리 최적화
<br>

✅ 소규모 데이터(5,000개 영상, 500건 조회수) 처리 **98% 성능 개선 (1시간 28분 → 8.6초)** <br>

| 단계 | 적용 사항 | 처리 시간 | 개선율 | 주요 개선 사항 |
|------|-----------|--------|------|--------|
| 최적화 전 | - | 1시간 28분 44초 | - | - |
| 1차 최적화 | Partitioning | 1시간 27분 50초 | 1.1% | 병렬 처리를 통해 배치 속도 개선 |
| 2차 최적화 | DB 인덱싱 | 9,431ms | 89.2% | 조회 쿼리 성능 개선 |
| 3차 최적화 | JDBC Bulk Insert | 8,639ms | 8.4% | DB 쓰기 성능 최적화 |
<br>

✅ 대규모 데이터(70,000개 영상, 1억건 조회수) 처리 **35% 성능 개선 (3분 39초 → 2분 23초)** `[DB 인덱스 최적화 적용 후 테스트]` 

| 단계 | 적용 사항 | 처리 시간 | 개선율 | 주요 개선 사항 |
|------|-----------|--------|------|--------|
| 최적화 전 | - | 3분 39초 | - | - |
| 1차 최적화 | Partitioning | 2분 39초 | 27.3% | 병렬 처리를 통해 배치 속도 개선
| 2차 최적화 | JDBC Bulk Insert | 2분 23초 | 10.1% | DB 쓰기 성능 최적화 |
<br>

[**🔗 [상세내용] 스트리밍 정산 및 통계 배치 단계별 최적화**](https://github.com/younghyun-j/streaming-settlement/wiki/%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8D-%EC%A0%95%EC%82%B0-%EB%B0%8F-%ED%86%B5%EA%B3%84-%EB%B0%B0%EC%B9%98-%EB%8B%A8%EA%B3%84%EB%B3%84-%EC%B5%9C%EC%A0%81%ED%99%94)

<br>

### 2. 영상 조회수 업데이트 최적화
> Redis 캐싱과 JDBC Bulk Insert 적용을 통한 DB 부하 감소 및 성능 최적화
<br>

✅ **영상 아이디별 분 단위 조회수 카운트 캐싱**
- Redis 도입으로 조회수 카운팅을 위한 DB 부하 감소
- 성능 최적화를 통해 대규모 트래픽에도 안정적인 조회수 처리 가능
<br>

✅ **스케줄러를 통한 주기적인 조회수 Bulk Insert**
- JDBC Bulk Insert를 활용하여 영상별 조회수를 한 번에 업데이트
- 쿼리 수를 최소화하고 DB 업데이트 성능 최적화 
<br>

[**🔗 [상세내용] Redis 캐싱과 JDBC Bulk Insert를 통한 영상 조회수 업데이트 최적화**](https://github.com/younghyun-j/streaming-settlement/wiki/Redis-%EC%BA%90%EC%8B%B1%EA%B3%BC-JDBC-Bulk-Insert%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%98%81%EC%83%81-%EC%A1%B0%ED%9A%8C%EC%88%98-%EC%97%85%EB%8D%B0%EC%9D%B4%ED%8A%B8-%EC%B5%9C%EC%A0%81%ED%99%94)

<br>

## ⚖️ 기술적 의사결정

- [**Redis 캐싱을 통한 성능 최적화**](https://github.com/younghyun-j/streaming-settlement/wiki/Redis-%EC%BA%90%EC%8B%B1%EC%9D%84-%ED%86%B5%ED%95%9C-%EC%84%B1%EB%8A%A5-%EC%B5%9C%EC%A0%81%ED%99%94) <br>
- [**스트리밍 어뷰징 동시성 처리에 Redisson을 선택한 이유**](https://github.com/younghyun-j/streaming-settlement/wiki/%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8D-%EC%96%B4%EB%B7%B0%EC%A7%95-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%B2%98%EB%A6%AC%EC%97%90-Redisson%EC%9D%84-%EC%84%A0%ED%83%9D%ED%95%9C-%EC%9D%B4%EC%9C%A0) <br>

<br>

## 🚀 트러블 슈팅

- [**트랜잭션 전파로 인한 Undo Log 누적으로 배치 처리 시 성능 저하 문제**](https://github.com/younghyun-j/streaming-settlement/wiki/%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EC%A0%84%ED%8C%8C%EB%A1%9C-%EC%9D%B8%ED%95%9C-Undo-Log-%EB%88%84%EC%A0%81%EC%9C%BC%EB%A1%9C-%EB%B0%B0%EC%B9%98-%EC%B2%98%EB%A6%AC-%EC%8B%9C-%EC%84%B1%EB%8A%A5-%EC%A0%80%ED%95%98-%EB%AC%B8%EC%A0%9C)
- [**스트리밍 어뷰징 동시성 문제 해결**](https://github.com/younghyun-j/streaming-settlement/wiki/%EC%8A%A4%ED%8A%B8%EB%A6%AC%EB%B0%8D-%EC%96%B4%EB%B7%B0%EC%A7%95-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)
- [**대규모 일일 영상 시청 로그 집계 성능 최적화**](https://github.com/younghyun-j/streaming-settlement/wiki/%EB%8C%80%EA%B7%9C%EB%AA%A8-%EC%9D%BC%EC%9D%BC-%EC%98%81%EC%83%81-%EC%8B%9C%EC%B2%AD-%EB%A1%9C%EA%B7%B8-%EC%A7%91%EA%B3%84-%EC%84%B1%EB%8A%A5-%EC%B5%9C%EC%A0%81%ED%99%94)

<br>

## 🛠️ 기술 스택

- **Language** : ![Java](https://img.shields.io/badge/Java17-%23ED8B00.svg?style=square&logo=openjdk&logoColor=white) <br>
- **Framework** : <img src = "https://img.shields.io/badge/Springboot 3.3.5-6DB33F?&logo=springboot&logoColor=white"> <img src = "https://img.shields.io/badge/Spring Batch 5 -6DB33F?&logo=Spring&logoColor=white"> ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=square&logo=Spring&logoColor=white) <br>
- **Build** : ![Gradle](https://img.shields.io/badge/Gradle%208-02303A.svg?style=square&logo=Gradle&logoColor=white)
- **Database** : <img src = "https://img.shields.io/badge/MySQL 8-4479A1?&logo=MySQL&logoColor=white"> <img src = "https://img.shields.io/badge/Redis-FF4438?&logo=redis&logoColor=white">
- **DevOps** :<img src = "https://img.shields.io/badge/Docker-2496ED?&logo=docker&logoColor=white">
- **IDE** : <img src = "https://img.shields.io/badge/Intellij Idea-000000?&logo=intellijidea&logoColor=white">
- **Version Control** : <img src = "https://img.shields.io/badge/Git-F05032?&logo=git&logoColor=white"> <img src = "https://img.shields.io/badge/Github-181717?&logo=github&logoColor=white">
<br>

## 📄 ERD
![image](https://github.com/user-attachments/assets/3d357bf3-9b7e-42eb-82a2-1a4a244f3776)



