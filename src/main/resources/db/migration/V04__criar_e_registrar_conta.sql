CREATE SEQUENCE conta_seq;

CREATE TABLE CONTA (
	id BIGINT DEFAULT conta_seq.nextval PRIMARY KEY,
	ativa BOOLEAN NOT NULL,
	digito VARCHAR(255),
	numero VARCHAR(255),
	saldo DECIMAL(18,6),
	agencia_id BIGINT,
	titular_id BIGINT,
	FOREIGN KEY (agencia_id) REFERENCES agencia(id),
	FOREIGN KEY (titular_id) REFERENCES cliente(id)
);

INSERT INTO CONTA VALUES(conta_seq.nextval, true, '0', '10000', '100.0', 1, 1);