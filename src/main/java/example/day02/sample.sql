DROP DATABASE IF EXISTS mydb0826;
CREATE DATABASE mydb0826;
USE mydb0826;
CREATE TABLE board( 
    no int AUTO_INCREMENT , 
    content VARCHAR(255) ,
    writer VARCHAR(30) ,
    constraint PRIMARY KEY( no ) 
);
insert into board( content, writer )values( "안녕하세요", "유재석" ),( "하하", "강호동"); -- 샘플 데이터 2개 