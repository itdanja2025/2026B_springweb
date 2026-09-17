USE practice6;

-- 카테고리 샘플 데이터
INSERT INTO category (cno, name) VALUES (1, '전자기기');
INSERT INTO category (cno, name) VALUES (2, '생활가전');
INSERT INTO category (cno, name) VALUES (3, '도서');

-- 제품 샘플 데이터
INSERT INTO product (bno, name, price, cno) VALUES (1, '기계식 키보드', 89000, 1);
INSERT INTO product (bno, name, price, cno) VALUES (2, '무선 마우스', 45000, 1);
INSERT INTO product (bno, name, price, cno) VALUES (3, '초음파 가습기', 32000, 2);

-- 리뷰 샘플 데이터
INSERT INTO review (rno, bno, reviewer, content, rating) VALUES (1, 1, '김철수', '키감이 매우 쫀득하고 좋습니다.', 5);
INSERT INTO review (rno, bno, reviewer, content, rating) VALUES (2, 1, '이영희', '소음이 적어서 사무실용으로 딱입니다.', 4);
INSERT INTO review (rno, bno, reviewer, content, rating) VALUES (3, 2, '박민수', '손목이 편안하고 배터리가 오래 가네요.', 5);