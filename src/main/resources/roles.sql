
drop database api_spring_security_jwt_v3;

show databases;

use api_spring_security_jwt_v3;

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_PM');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

select * from roles;

select * from users;

select * from user_roles;
