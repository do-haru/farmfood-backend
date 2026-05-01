# API

## 1. 관리자용 검색 수집 및 저장 API
POST http://localhost:8080/api/admin/search-contents/collect/seeds

## 2. 관리자용 키퉈드 추출 API
POST http://localhost:8080/api/admin/keywords/extract

## 3. 관리자용 키워드 통계 지표 수집 API
POST http://localhost:8080/api/admin/metrics/shopping-trends/collect

## 4. 관리자용 키워드 순위 산출 API
POST http://localhost:8080/api/admin/rankings/calculate

#  SQL

## 1. search_content 테이블 조회 및 초기화
truncate table search_content restart identity;
select * from search_content

## 2. extracted_keyword 테이블 조회 및 초기화
truncate table extracted_keyword restart identity;
select * from extracted_keyword order by frequency desc;

## 3. keyword_trend_metric 테이블 조회 및 초기화
truncate table keyword_trend_metric restart identity;
select * from keyword_trend_metric order by frequency desc;

## 4. trend_keyword_ranking 테이블 조회 및 초기화
truncate table trend_keyword_ranking restart identity;
select * from trend_keyword_ranking order by frequency desc;
