-- Mesmo modelo do sql/schema.sql (MySQL), adaptado para o H2 embutido.
-- Executado automaticamente a cada conexao, por isso usa IF NOT EXISTS / MERGE.
CREATE TABLE IF NOT EXISTS filme (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    titulo      VARCHAR(150)  NOT NULL,
    diretor     VARCHAR(100)  NOT NULL,
    ano         SMALLINT      NOT NULL,
    genero      VARCHAR(30)   NOT NULL,
    duracao_min SMALLINT      NOT NULL,
    nota        DECIMAL(3,1)  NULL,
    sinopse     VARCHAR(1000) NULL,
    criado_em   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_filme_ano     CHECK (ano BETWEEN 1888 AND 2100),
    CONSTRAINT ck_filme_duracao CHECK (duracao_min BETWEEN 1 AND 999),
    CONSTRAINT ck_filme_nota    CHECK (nota IS NULL OR nota BETWEEN 0 AND 10),
    CONSTRAINT uk_filme_titulo_ano UNIQUE (titulo, ano)
);

-- Dados de exemplo inseridos so na primeira vez (chave: titulo + ano)
MERGE INTO filme (titulo, diretor, ano, genero, duracao_min, nota, sinopse) KEY (titulo, ano) VALUES
('Cidade de Deus', 'Fernando Meirelles', 2002, 'DRAMA', 130, 9.0,
 'A trajetoria de jovens na Cidade de Deus, no Rio de Janeiro, entre as decadas de 60 e 80.'),
('O Auto da Compadecida', 'Guel Arraes', 2000, 'COMEDIA', 104, 9.2,
 'As aventuras de Joao Grilo e Chico no sertao nordestino.'),
('Central do Brasil', 'Walter Salles', 1998, 'DRAMA', 113, 8.5,
 'Uma escrevedora de cartas acompanha um menino em busca do pai pelo interior do Brasil.');
