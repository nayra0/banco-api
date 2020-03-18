CREATE SEQUENCE banco_seq;

CREATE TABLE BANCO (
	id BIGINT DEFAULT banco_seq.nextval PRIMARY KEY,
	codigo VARCHAR(255),
 	nome VARCHAR(255)
);

INSERT INTO BANCO VALUES(banco_seq.nextval, '001', 'Banco do Brasil');
INSERT INTO BANCO VALUES(banco_seq.nextval, '104', 'Caixa Econômica');