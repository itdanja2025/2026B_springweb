DROP DATABASE IF EXISTS mydb0903;
CREATE DATABASE mydb0903;
USE mydb0903;
create table test( 
    no int PRIMARY key AUTO_INCREMENT, 
    content VARCHAR(255) , 
    writer VARCHAR(255)
);
