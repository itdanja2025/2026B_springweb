drop DATABASE if EXISTS mydb0902;
create database mydb0902;
use mydb0902;
create table exam(
    eno INT auto_increment PRIMARY KEY ,
    ename VARCHAR(255)
);
INSERT INTO exam( ename )values( '유재석' );
INSERT INTO exam( ename )values( '강호동' );
INSERT INTO exam( ename )values( '신동엽' );