# API

## 1. 키워드 검색

### 1-1. 관리자용 검색 수집 및 저장 API
POST http://localhost:8080/api/admin/search-contents/collect/seeds

### 1-2 search_content 테이블 조회 및 초기화
truncate table search_content restart identity;
select * from search_content

## 2. 키워드 추출

### 2-1. 관리자용 키워드 추출 API
POST http://localhost:8080/api/admin/keywords/extract

## 2-2. extracted_keyword 테이블 조회 및 초기화
truncate table extracted_keyword restart identity;
select * from extracted_keyword order by frequency desc;

## 3. 관리자용 키워드 통계 지표 수집 API
POST http://localhost:8080/api/admin/metrics/shopping-trends/collect
POST http://localhost:8080/api/admin/metrics/search-counts/collect
POST http://localhost:8080/api/admin/metrics/youtube/collect

## 4. 관리자용 키워드 순위 산출 API
POST http://localhost:8080/api/admin/rankings/calculate

## 5. 키워드 랭킹 출력 API
GET http://localhost:8080/api/dashboard/rankings

## 6. 키워드 쇼핑 트렌드 지표 출력 API
GET http://localhost:8080/api/dashboard/keywords/참외/shopping-trends

#  SQL





## 3-1. keyword_trend_metric 테이블 조회 및 초기화 (Naver 쇼핑 트렌드 지표)
truncate table keyword_trend_metric restart identity;
select * from keyword_trend_metric;

## 3-2. keyword_trend_metric 테이블 조회 및 초기화 (Naver 쇼핑 트렌드 지표)
select * from youtube_keyword_metric;

## 4. trend_keyword_ranking 테이블 조회 및 초기화
truncate table trend_keyword_ranking restart identity;
select * from trend_keyword_ranking order by rank asc;

truncate table keyword_daily_search_estimate restart identity;
select * from keyword_daily_search_estimate;

truncate table keyword_search_count restart identity;
select * from keyword_search_count;