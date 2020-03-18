CREATE SEQUENCE agencia_seq;

CREATE TABLE AGENCIA(
    id BIGINT DEFAULT agencia_seq.nextval PRIMARY KEY,
    codigo VARCHAR(255),
    digito VARCHAR(255),
    banco_id BIGINT,
    FOREIGN KEY (banco_id) REFERENCES banco(id)
);

INSERT INTO AGENCIA VALUES(agencia_seq.nextval, '001', 'x', 1);
INSERT INTO AGENCIA VALUES(agencia_seq.nextval, '100', 'x', 2);