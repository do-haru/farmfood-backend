# API

## 1. 관리자용 검색 수집 및 저장 API
POST http://localhost:8080/api/admin/search-contents/collect/seeds

## 2. 관리자용 키퉈드 추출 API
POST http://localhost:8080/api/admin/keywords/extract


#  SQL

## 1. search_content 테이블 조회 및 초기화
truncate table search_content restart identity;
select * from search_content

## 2. extracted_keyword 테이블 조회 및 초기화
truncate table extracted_keyword restart identity;
select * from extracted_keyword order by frequency desc;
