CREATE SEQUENCE cliente_seq;

CREATE TABLE CLIENTE(
    id BIGINT DEFAULT cliente_seq.nextval PRIMARY KEY,
    CPF VARCHAR(255),
    NOME VARCHAR(255)    
);

INSERT INTO CLIENTE VALUES(cliente_seq.nextval, '85449914083', 'Michael Douglas');
INSERT INTO CLIENTE VALUES(cliente_seq.nextval, '80178655031', 'Beatriz Oliveira');