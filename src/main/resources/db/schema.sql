/****************** Configuration de la Base ******************/
-- Création de la base
DROP DATABASE IF EXISTS chat_book;
CREATE DATABASE chat_book CHARACTER SET 'utf8';
USE chat_book;

/****************** Création des tables ******************/
-- Table des utilisateurs
DROP TABLE IF EXISTS USER;
CREATE TABLE USER (
	id int NOT NULL AUTO_INCREMENT primary key,
	login varchar(15) NOT NULL UNIQUE,
	firstname varchar(50) NOT NULL,
	lastname varchar(50) NOT NULL,
	password varchar(100) NOT NULL,
	role varchar(12) NOT NULL,
	constraint u_role_enum check(role in ('USER_DEFAULT','USER_ADMIN'))
);

-- Table de relation entre deux utilisateurs amis
DROP TABLE IF EXISTS FRIENDSHIP;
CREATE TABLE FRIENDSHIP (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_user1 int NOT NULL,
	id_user2 int NOT NULL,
	confirmed boolean,
	foreign key (id_user1) REFERENCES USER(id) ON DELETE CASCADE,
	foreign key (id_user2) REFERENCES USER(id) ON DELETE CASCADE
);

-- Table des groupes de discussions
DROP TABLE IF EXISTS CONNECTION;
CREATE TABLE CONNECTION (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_mod int,
	name varchar(9) NOT NULL,
	foreign key (id_mod) REFERENCES USER(id) ON DELETE CASCADE
);

-- Table de relation entre un utilisateur et un groupe de discussion
DROP TABLE IF EXISTS USER_CONNECTION;
CREATE TABLE USER_CONNECTION (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_connection int NOT NULL,
	id_user int NOT NULL,
	foreign key (id_connection) REFERENCES CONNECTION(id) ON DELETE CASCADE
);

-- Table des messages
DROP TABLE IF EXISTS MESSAGE;
CREATE TABLE MESSAGE (
	id int NOT NULL AUTO_INCREMENT primary key,
	id_connection int NOT NULL,
	id_user int NOT NULL,
	message varchar(140) NOT NULL,
	prioritaire boolean,
	expiration date,
	foreign key (id_connection) REFERENCES CONNECTION(id),
	foreign key (id_user) REFERENCES USER(id) ON DELETE CASCADE
);