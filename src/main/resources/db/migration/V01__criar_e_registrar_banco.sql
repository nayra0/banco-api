CREATE SEQUENCE banco_seq;

CREATE TABLE BANCO (
	id BIGINT DEFAULT banco_seq.nextval PRIMARY KEY,
	codigo VARCHAR(255),
 	nome VARCHAR(255)
);

INSERT INTO BANCO VALUES(1, '001', 'Banco do Brasil');
INSERT INTO BANCO VALUES(2, '104', 'Caixa Econômica');