-- practice4 데이터베이스 초기 실행용 SQL (Insert 코드)
-- 1. 과정(Course) 정보 등록
INSERT INTO course (course_name, created_at, updated_at) VALUES ('자바 백엔드 개발자 과정', NOW(), NOW());
INSERT INTO course (course_name, created_at, updated_at) VALUES ('프론트엔드 React 과정', NOW(), NOW());
-- 2. 학생(Student) 정보 등록
INSERT INTO student (student_name, created_at, updated_at) VALUES ('김철수', NOW(), NOW());
INSERT INTO student (student_name, created_at, updated_at) VALUES ('이영희', NOW(), NOW());
-- 3. 수강(Enroll) 정보 등록
INSERT INTO enroll (status, course_id, student_id, created_at, updated_at) VALUES ('수강중', 1, 1, NOW(), NOW());
INSERT INTO enroll (status, course_id, student_id, created_at, updated_at) VALUES ('수강중', 1, 2, NOW(), NOW());