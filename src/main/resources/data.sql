-- ─────────────────────────────────────────
-- 1. ENDEREÇO  (obrigatório antes de usuario)
-- ─────────────────────────────────────────
INSERT INTO endereco (id, cep, logradouro, bairro, cidade, uf, numero, complemento) VALUES
(1, '01310-100', 'Avenida Paulista',        'Bela Vista',          'São Paulo',   'SP', '1000', 'Apto 42'),
(2, '04038-000', 'Rua Domingos de Moraes',  'Vila Mariana',        'São Paulo',   'SP', '320',  NULL),
(3, '22041-001', 'Rua Visconde de Pirajá',  'Ipanema',             'Rio de Janeiro','RJ','500', 'Sala 3'),
(4, '30130-110', 'Avenida Afonso Pena',     'Centro',              'Belo Horizonte','MG','707', NULL),
(5, '07000-000', 'Rua das Flores',          'Centro',              'Guarulhos',   'SP', '123',  NULL);


-- ─────────────────────────────────────────
-- 2. CARGO
-- ─────────────────────────────────────────
INSERT INTO cargo (id, nome, descricao) VALUES
(1, 'Administrador',  'Acesso total ao sistema'),
(2, 'Recepcionista',  'Gerencia agendamentos'),
(3, 'Acupunturista',  'Realiza atendimentos');


-- ─────────────────────────────────────────
-- 3. PERMISSÕES
-- ─────────────────────────────────────────
INSERT INTO permissoes (id, nome, descricao) VALUES
(1, 'CRUD_USUARIO',         'Gerenciar usuários'),
(2, 'CRUD_AGENDAMENTO',     'Gerenciar agendamentos'),
(3, 'REALIZAR_ATENDIMENTO', 'Executar atendimentos');


-- ─────────────────────────────────────────
-- 4. PERMISSÕES_CARGO
--    Admin: todas | Recepcionista: agendamento | Acupunturista: atendimento
-- ─────────────────────────────────────────
INSERT INTO permissoes_cargo (fkPermissoes, fkCargo) VALUES
(1, 1),
(2, 1),
(3, 1),
(2, 2),
(3, 3);


-- ─────────────────────────────────────────
-- 5. STATUS do agendamento
-- ─────────────────────────────────────────
INSERT INTO status (id, nome) VALUES
(1, 'Agendado'),
(2, 'Confirmado'),
(3, 'Cancelado'),
(4, 'Finalizado');


-- ─────────────────────────────────────────
-- 6. SALA
-- ─────────────────────────────────────────
INSERT INTO sala (id, descricao) VALUES
(1, 'Sala 1'),
(2, 'Sala 2'),
(3, 'Sala Online');


-- ─────────────────────────────────────────
-- 7. ESPECIALIDADE
-- ─────────────────────────────────────────
INSERT INTO especialidade (id, nome) VALUES
 (1, 'Dor muscular'),
 (2, 'Ansiedade'),
 (3, 'Insônia'),
 (4, 'Reabilitação');


-- ─────────────────────────────────────────
-- 8. SERVIÇO
-- ─────────────────────────────────────────
INSERT INTO servico (id, nome, valor, descricao, tempoMedio) VALUES
(1, 'Sessão de Acupuntura', 120.00, 'Sessão padrão de acupuntura sistêmica',        60),
(2, 'Auriculoterapia',       80.00, 'Tratamento auricular com sementes ou agulhas',  40),
(3, 'Ventosaterapia',        90.00, 'Terapia com ventosas para alívio muscular',     50),
(4, 'Acupuntura Online',     70.00, 'Orientação e acompanhamento remoto',            30);


-- ─────────────────────────────────────────
-- 9. ESPECIALIDADE_SERVICO
--    Qual especialidade cobre quais serviços
-- ─────────────────────────────────────────
INSERT INTO especialidade_servico (fkEspecialidade, fkServico) VALUES
(1, 1),  -- Dor muscular    → Sessão de Acupuntura
(1, 3),  -- Dor muscular    → Ventosaterapia
(2, 1),  -- Ansiedade       → Sessão de Acupuntura
(2, 2),  -- Ansiedade       → Auriculoterapia
(3, 1),  -- Insônia         → Sessão de Acupuntura
(3, 2),  -- Insônia         → Auriculoterapia
(4, 3),  -- Reabilitação    → Ventosaterapia
(4, 4);  -- Reabilitação    → Acupuntura Online


-- ─────────────────────────────────────────
-- 10. SALA_SERVICO
--     Quais salas comportam quais serviços
-- ─────────────────────────────────────────
INSERT INTO sala_servico (fkSala, fkServico) VALUES
(1, 1),  -- Sala 1 → Sessão de Acupuntura
(1, 2),  -- Sala 1 → Auriculoterapia
(1, 3),  -- Sala 1 → Ventosaterapia
(2, 1),  -- Sala 2 → Sessão de Acupuntura
(2, 2),  -- Sala 2 → Auriculoterapia
(2, 3),  -- Sala 2 → Ventosaterapia
(3, 4);  -- Sala Online → Acupuntura Online


-- ─────────────────────────────────────────
-- 11. USUÁRIO
--     Senha de todos: "123456"
--     Hash BCrypt: $2a$12$LlrgS/ccNTbuAfIAGAJGZOjnPKnJRg7cGWX3KatA1EKltYXtVxR5S
-- ─────────────────────────────────────────
INSERT INTO usuario (id, nome, telefone, email, senha, data_nascimento, ativo, fkEndereco) VALUES
-- Funcionários
(1, 'Dr. Ricardo Silveira', '11999990001', 'ricardo@taotenshin.com',
 '$2a$12$LlrgS/ccNTbuAfIAGAJGZOjnPKnJRg7cGWX3KatA1EKltYXtVxR5S', '1985-03-15', 1, 1),
(2, 'Dra. Beatriz Mendes',  '11999990002', 'beatriz@taotenshin.com',
 '$2a$12$LlrgS/ccNTbuAfIAGAJGZOjnPKnJRg7cGWX3KatA1EKltYXtVxR5S', '1990-07-22', 1, 2),
-- Clientes
(3, 'Felipe Silva',         '11988880001', 'felipe@email.com',
 '$2a$12$LlrgS/ccNTbuAfIAGAJGZOjnPKnJRg7cGWX3KatA1EKltYXtVxR5S', '2000-05-10', 1, 3),
(4, 'Ana Paula Costa',      '11988880002', 'ana@email.com',
 '$2a$12$LlrgS/ccNTbuAfIAGAJGZOjnPKnJRg7cGWX3KatA1EKltYXtVxR5S', '1992-11-03', 1, 4);


-- ─────────────────────────────────────────
-- 12. FUNCIONÁRIO
-- ─────────────────────────────────────────
INSERT INTO funcionario (id, fkUsuario, fkCargo) VALUES
(1, 1, 3),  -- Dr. Ricardo → Acupunturista
(2, 2, 1);  -- Dra. Beatriz → Administrador


-- ─────────────────────────────────────────
-- 13. FUNCIONÁRIO_ESPECIALIDADE
-- ─────────────────────────────────────────
INSERT INTO funcionario_especialidade (fkFuncionario, fkEspecialidade) VALUES
(1, 1),  -- Dr. Ricardo  → Dor muscular
(1, 3),  -- Dr. Ricardo  → Insônia
(2, 2),  -- Dra. Beatriz → Ansiedade
(2, 4);  -- Dra. Beatriz → Reabilitação


-- ─────────────────────────────────────────
-- 14. AGENDA_FUNCIONARIO
--     dia_semana: 1=Domingo, 2=Segunda ... 7=Sábado (padrão MySQL DAYOFWEEK)
-- ─────────────────────────────────────────
INSERT INTO agenda_funcionario (id, fkFuncionario, dia_semana, hora_inicio, hora_fim) VALUES
-- Dr. Ricardo: Seg–Sex manhã e tarde
(1,  1, 2, '08:00:00', '12:00:00'),  -- Segunda manhã
(2,  1, 2, '14:00:00', '18:00:00'),  -- Segunda tarde
(3,  1, 3, '08:00:00', '12:00:00'),  -- Terça manhã
(4,  1, 3, '14:00:00', '18:00:00'),  -- Terça tarde
(5,  1, 4, '08:00:00', '12:00:00'),  -- Quarta manhã
(6,  1, 4, '14:00:00', '18:00:00'),  -- Quarta tarde
(7,  1, 5, '08:00:00', '12:00:00'),  -- Quinta manhã
(8,  1, 5, '14:00:00', '18:00:00'),  -- Quinta tarde
(9,  1, 6, '08:00:00', '12:00:00'),  -- Sexta manhã
-- Dra. Beatriz: Ter, Qui, Sáb
(10, 2, 3, '09:00:00', '17:00:00'),  -- Terça
(11, 2, 5, '09:00:00', '17:00:00'),  -- Quinta
(12, 2, 7, '09:00:00', '13:00:00');  -- Sábado


-- ─────────────────────────────────────────
-- 15. AGENDA_EXCECAO
--     Folga / dia indisponível / horário especial
-- ─────────────────────────────────────────
INSERT INTO agenda_excecao (id, fkFuncionario, data, hora_inicio, hora_fim, disponivel) VALUES
(1, 1, '2026-06-12', NULL,         NULL,         0),  -- Dr. Ricardo indisponível dia todo (feriado)
(2, 2, '2026-07-04', '09:00:00', '12:00:00',     0),  -- Dra. Beatriz só manhã indisponível
(3, 1, '2026-07-10', '14:00:00', '18:00:00',     1);  -- Dr. Ricardo: tarde extra disponível (reposição)


-- ─────────────────────────────────────────
-- 16. CLIENTE
-- ─────────────────────────────────────────
INSERT INTO cliente (id, fkUsuario, observacao) VALUES
(1, 3, 'Paciente com dores crônicas nas costas'),
(2, 4, 'Primeiro atendimento — indicação médica');


-- ─────────────────────────────────────────
-- 17. AGENDAMENTO
--     ATENÇÃO: novo schema NÃO tem fkFuncionario nem fkServico aqui.
--     Funcionário vai em funcionario_agendamento.
--     Serviço vai em atendimento_servico.
-- ─────────────────────────────────────────
INSERT INTO agendamento (id, data_hora_inicio, data_hora_fim, observacao, fkCliente, fkSala, fkStatus) VALUES
(1, '2026-06-02 08:00:00', '2026-06-02 09:00:00', 'Dores na lombar',            1, 1, 2),  -- Confirmado
(2, '2026-06-02 10:00:00', '2026-06-02 10:40:00', NULL,                         2, 2, 1),  -- Agendado
(3, '2026-06-03 09:00:00', '2026-06-03 10:00:00', 'Paciente ansioso, cuidado',  1, 1, 1),  -- Agendado
(4, '2026-05-28 14:00:00', '2026-05-28 15:00:00', 'Sessão de retorno',          2, 1, 4),  -- Finalizado
(5, '2026-06-05 14:00:00', '2026-06-05 14:30:00', 'Consulta online',            1, 3, 1);  -- Agendado Online


-- ─────────────────────────────────────────
-- 18. FUNCIONARIO_AGENDAMENTO
--     Vincula qual funcionário atende cada agendamento
-- ─────────────────────────────────────────
INSERT INTO funcionario_agendamento (fkFuncionario, fkAgendamento) VALUES
(1, 1),  -- Dr. Ricardo atende agendamento 1
(1, 2),  -- Dr. Ricardo atende agendamento 2
(2, 3),  -- Dra. Beatriz atende agendamento 3
(1, 4),  -- Dr. Ricardo atendeu agendamento 4 (finalizado)
(2, 5);  -- Dra. Beatriz atende agendamento 5 (online)


-- ─────────────────────────────────────────
-- 19. ATENDIMENTO_SERVICO
--     Vincula serviço(s) a cada agendamento (com valor e notas clínicas)
--     Apenas agendamentos confirmados ou finalizados costumam ter registro,
--     mas pode existir desde o agendamento para controle de valor.
-- ─────────────────────────────────────────
INSERT INTO atendimento_servico (id, valor_unitario, fkServico, fkAgendamento, descricao, observacoes) VALUES
(1, 120.00, 1, 1, 'Acupuntura sistêmica — região lombar',       'Paciente relatou melhora após sessão'),
(2,  80.00, 2, 2, 'Auriculoterapia — pontos de ansiedade',      NULL),
(3, 120.00, 1, 3, 'Acupuntura — protocolo para insônia',        'Usar agulhas de 0.25mm'),
(4, 120.00, 1, 4, 'Acupuntura — sessão de retorno completa',    'Paciente evoluiu bem'),
(5,  70.00, 4, 5, 'Acupuntura Online — orientação pós-sessão',  'Enviar protocolo por e-mail');