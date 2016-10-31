/****************** Configuration de la Base ******************/
-- creation de la base
DROP DATABASE IF EXISTS chat_book;
CREATE DATABASE chat_book CHARACTER SET 'utf8';
USE chat_book;

/****************** Création des tables ******************/
-- table des utilisateurs
DROP TABLE IF EXISTS USER;
CREATE TABLE USER (
	id int NOT NULL AUTO_INCREMENT primary key,
	login varchar(15) NOT NULL UNIQUE,
	firstname varchar(11) NOT NULL,
	surname varchar(11) NOT NULL,
	password varchar(15) NOT NULL,
	role varchar(12) NOT NULL,
	constraint u_role_enum check(role in ('USER_DEFAULT','USER_ADMIN'))
);

-- table des groupes de centres d'intêrets
DROP TABLE IF EXISTS HOBBIES;
CREATE TABLE HOBBIES (
	id int NOT NULL AUTO_INCREMENT primary key,
	name varchar(9) NOT NULL
);

-- table des centres d'intêrets
DROP TABLE IF EXISTS HOBBY;
CREATE TABLE HOBBY (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_hobbies int NOT NULL,
	name varchar(9) NOT NULL,
	foreign key (id_hobbies) REFERENCES HOBBIES(id)
);

-- table de relation entre un utilisateur et un groupe de centres d'interets
DROP TABLE IF EXISTS USER_HOBBIES;
CREATE TABLE USER_HOBBIES (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_user int NOT NULL,
	id_hobbies int NOT NULL,
	foreign key (id_user) REFERENCES USER(id),
	foreign key (id_hobbies) REFERENCES HOBBIES(id)
);

-- table de relation entre un utilisateur et un centre d'interet
DROP TABLE IF EXISTS USER_HOBBY;
CREATE TABLE USER_HOBBY (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_user int NOT NULL,
	id_hobby int NOT NULL,
	foreign key (id_user) REFERENCES USER(id),
	foreign key (id_hobby) REFERENCES HOBBY(id)
);

-- table de relation entre deux utilisateurs amis
DROP TABLE IF EXISTS FRIEND;
CREATE TABLE FRIEND (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_user1 int NOT NULL,
	id_user2 int NOT NULL,
	confirmed boolean,
	foreign key (id_user1) REFERENCES USER(id),
	foreign key (id_user2) REFERENCES USER(id)
);

-- table des groupes de discutions
DROP TABLE IF EXISTS CONNECTION;
CREATE TABLE CONNECTION (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_mod int,
	name varchar(9) NOT NULL,
	foreign key (id_mod) REFERENCES USER(id)
);

-- table de relation entre un utilisateur et un groupe de discution
DROP TABLE IF EXISTS USER_CONNECTION;
CREATE TABLE USER_CONNECTION (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_connection int NOT NULL,
	id_user int NOT NULL,
	foreign key (id_connection) REFERENCES CONNECTION(id)
);

-- table des messages
DROP TABLE IF EXISTS MESSAGE;
CREATE TABLE MESSAGE (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_connection int NOT NULL,
	id_user int NOT NULL,
	message varchar(140) NOT NULL,
	prioritaire boolean,
	expiration date,
	foreign key (id_connection) REFERENCES CONNECTION(id),
	foreign key (id_user) REFERENCES USER(id)
);