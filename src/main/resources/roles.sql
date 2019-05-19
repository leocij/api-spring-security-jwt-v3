
drop database api_spring_security_jwt_v3;

show databases;

use api_spring_security_jwt_v3;

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_PM');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

-- password: admin123
INSERT INTO users (EMAIL, NAME, PASSWORD, USERNAME) VALUES ("admin@email.com", "Admin", "$2a$10$U85.QohgfuurJl6X8TXrtO/TYob/aK4FqxjR7mMXmedG6dIzIhKGe", "admin");

INSERT INTO user_roles (USER_ID, ROLE_ID) values (1, 3);

-- password: user123
INSERT INTO users (EMAIL, NAME, PASSWORD, USERNAME) VALUES ("user@email.com", "User", "$2a$10$a5XZVqKG.eyVBCiRuBZzcu7UUjFhApQWyB4LW5ULoq1ewq1Uo38Ai", "user");

INSERT INTO user_roles (USER_ID, ROLE_ID) values (2, 1);

select * from roles;

select * from users;

select * from user_roles;


