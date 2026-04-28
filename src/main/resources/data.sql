INSERT INTO cliente (nome, email, senha) VALUES ('Felipe', 'felipe@email.com', '$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC');
-- senha: 123456
INSERT INTO cliente (nome, email, senha) VALUES ('Joao', 'joao@email.com', '$2a$10$0hhaL8kQk.YOv3PbzGiHdOdkpn.AN/MsdZiCd1GApegkA1hjPlQnu');
-- senha: senha123
INSERT INTO agendamento (data_hora, fk_cliente)
VALUES ('2026-05-01 14:00:00', 1);

INSERT INTO agendamento (data_hora, fk_cliente)
VALUES ('2026-05-01 15:00:00', 2);