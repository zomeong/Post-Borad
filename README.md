# Post-Borad

## :envelope: Introduction
spring boot로 CRUD를 구현한 게시판 서버 만들기

## :memo: 설계
- Usecase Diagram<br>
![image](https://github.com/zomeong/Post-Borad/assets/123639754/0caf5c2c-9f4a-4332-86a3-6cb48fcd9f87)

- API 명세서
<br>[postman API 명세서](http://documenter.getpostman.com/view/30860889/2s9YXfbNqs)<br>

- ERD<br>
![ERD](https://github.com/zomeong/Post-Borad/assets/123639754/729bf74a-059b-44c1-8ec7-1902dc84e391)

## :computer: 개발 환경
- JDK 17
- IDE : IntelliJ
- FRAMWORK : Spring Boot
- DB : MY SQL

## :sparkler: 주요 기능
- 회원가입, 로그인 (spring security jwt 방식)
- 포스트 작성
- 전체 포스트 목록 조회
- 포스트 선택 조회
- 포스트 수정
- 포스트 완료 처리
- 포스트 비공개 처리
- 포스트 제목 검색
- 댓글 등록
- 댓글 수정
- 댓글 삭제