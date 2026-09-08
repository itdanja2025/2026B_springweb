-- 1. 게시글(Board) 샘플 데이터 등록
INSERT INTO board (author, password, content, created_at, updated_at)
VALUES ('유재석', '1234', '안녕하세요! 첫 번째 게시글입니다. 반갑습니다.', NOW(), NOW());
INSERT INTO board (author, password, content, created_at, updated_at)
VALUES ('강호동', '1234', 'JPA 피드 게시판 테스트 글입니다. 잘 부탁드립니다~', NOW(), NOW());
INSERT INTO board (author, password, content, created_at, updated_at)
VALUES ('신동엽', '1234', '주말 잘 보내세요! 재미있는 스프링부트 & JPA 수업입니다.', NOW(), NOW());
INSERT INTO comment (author, password, content, board_id, created_at, updated_at)
VALUES ('박명수', '1234', '첫 게시글 축하드립니다!', 1, NOW(), NOW());
INSERT INTO comment (author, password, content, board_id, created_at, updated_at)
VALUES ('하하', '1234', '반가워요 재석이형~', 1, NOW(), NOW());
INSERT INTO comment (author, password, content, board_id, created_at, updated_at)
VALUES ('이수근', '1234', '호동이형 화이팅입니다!', 2, NOW(), NOW());
INSERT INTO comment (author, password, content, board_id, created_at, updated_at)
VALUES ('김영철', '1234', '당당당당 당당당~ 응원합니다!', 2, NOW(), NOW());