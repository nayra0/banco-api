CREATE SEQUENCE transacao_seq;

CREATE TABLE TRANSACAO (
	id BIGINT DEFAULT transacao_seq.nextval PRIMARY KEY, 
	data_hora TIMESTAMP, 
	tipo_transacao VARCHAR, 
	valor DECIMAL(18,6), 
	conta_destino_id BIGINT, 
	conta_origem_id BIGINT,
	FOREIGN KEY (conta_destino_id) REFERENCES conta(id),
	FOREIGN KEY (conta_origem_id) REFERENCES conta(id)
);

INSERT INTO TRANSACAO (id, data_hora, tipo_transacao, valor) 
VALUES(transacao_seq.nextval, parsedatetime('14-03-2020 14:00:00.000', 'dd-MM-yyyy hh:mm:ss.SS'), 'DEPOSITO', '100.00');

CREATE TABLE CONTA_TRANSACOES (
	conta_id BIGINT NOT NULL, 
	transacoes_id BIGINT NOT NULL,
	FOREIGN KEY (conta_id) REFERENCES conta(id),
	FOREIGN KEY (transacoes_id) REFERENCES transacao(id)
);

INSERT INTO CONTA_TRANSACOES VALUES(1, 1);