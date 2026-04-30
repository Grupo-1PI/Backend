
-- ENDERECO (OBRIGATORIO PRIMEIRO)
INSERT INTO endereco (id, cep, logradouro, bairro, cidade, uf, numero, complemento) VALUES
(1, '07000-000', 'Rua A', 'Centro', 'Guarulhos', 'SP', '123', NULL);

-- CARGO
INSERT INTO cargo (id, nome, descricao) VALUES
(1, 'Administrador', 'Acesso total ao sistema'),
(2, 'Recepcionista', 'Gerencia agendamentos'),
(3, 'Acupunturista', 'Realiza atendimentos');

-- PERMISSOES
INSERT INTO permissoes (id, nome, descricao) VALUES
(1, 'CRUD_USUARIO', 'Gerenciar usuários'),
(2, 'CRUD_AGENDAMENTO', 'Gerenciar agendamentos'),
(3, 'REALIZAR_ATENDIMENTO', 'Executar atendimentos');

-- PERMISSOES_CARGO
INSERT INTO permissoes_cargo (fkPermissoes, fkCargo) VALUES
(1,1),
(2,1),
(3,1),
(2,2),
(3,3);

-- STATUS
INSERT INTO status (id, nome) VALUES
(1, 'Agendado'),
(2, 'Confirmado'),
(3, 'Cancelado'),
(4, 'Finalizado');

-- SALA
INSERT INTO sala (id, descricao) VALUES
(1, 'Sala 1'),
(2, 'Sala 2');

-- ESPECIALIDADE
INSERT INTO especialidade (id, nome) VALUES
(1, 'Dor muscular'),
(2, 'Ansiedade'),
(3, 'Insônia');

-- SERVICO
INSERT INTO servico (id, nome, valor, descricao, tempoMedio) VALUES
(1, 'Sessão de Acupuntura', 120.00, 'Sessão padrão', 60),
(2, 'Auriculoterapia', 80.00, 'Tratamento auricular', 40);

-- ESPECIALIDADE_SERVICO
INSERT INTO especialidade_servico (fkEspecialidade, fkServico) VALUES
(1,1),
(2,1),
(3,1),
(2,2);

-- USUARIO (AGORA FUNCIONA)
INSERT INTO usuario (id, nome, telefone, email, senha, data_nascimento, ativo, fkEndereco) VALUES
(1, 'Felipe', '11999999999', 'felipe@email.com', '$2a$12$LlrgS/ccNTbuAfIAGAJGZOjnPKnJRg7cGWX3KatA1EKltYXtVxR5S', '2000-05-10', 1, 1),
(2, 'Joao', '11988888888', 'joao@email.com', '$2a$12$LlrgS/ccNTbuAfIAGAJGZOjnPKnJRg7cGWX3KatA1EKltYXtVxR5S', '1995-03-20', 1, 1);

-- CLIENTE
INSERT INTO cliente (id, fkUsuario, observacao) VALUES
(1, 1, 'VIP'),
(2, 2, 'Novo cliente');

-- FUNCIONARIO
INSERT INTO funcionario (id, fkUsuario, fkCargo) VALUES
(1, 1, 1);

-- AGENDAMENTO
INSERT INTO agendamento (
id,
data_hora_inicio,
data_hora_fim,
fkCliente,
fkFuncionario,
fkSala,
fkServico,
fkStatus
) VALUES
(1, '2026-05-01 14:00:00', '2026-05-01 15:00:00', 1, 1, 1, 1, 1);

-- ATENDIMENTO
INSERT INTO atendimento (id, fkAgendamento, descricao, observacoes) VALUES
(1, 1, 'Atendimento inicial', 'Paciente com dor nas costas');

-- ATENDIMENTO_SERVICO
INSERT INTO atendimento_servico (id, valor_unitario, fkAtendimento, fkServico) VALUES
(1, 120.00, 1, 1);

-- FUNCIONARIO_ESPECIALIDADE
INSERT INTO funcionario_especialidade (fkFuncionario, fkEspecialidade) VALUES
(1,1),
(1,2);