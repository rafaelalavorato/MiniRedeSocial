create database redesocial0;
CREATE TABLE usuarios (
  nome VARCHAR(255) NOT NULL,
  senha VARCHAR(20) NOT NULL,
  email VARCHAR(255) NOT NULL
  );
CREATE TABLE amigos (
  usuario1 VARCHAR(70) NOT NULL,
  usuario2 VARCHAR(70) NOT NULL
  );
CREATE TABLE enviarmensagem (
  primeirousuario VARCHAR(200) NOT NULL,
  segundousuario VARCHAR(200) NOT NULL,
  mensagem VARCHAR(255) NOT NULL,
  PRIMARY KEY (primeirousuario, segundousuario)
);