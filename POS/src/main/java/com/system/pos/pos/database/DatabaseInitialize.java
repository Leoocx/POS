package com.system.pos.pos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitialize {
    private static final String URL = "jdbc:sqlite:database.db";

    public static void criarTabelas() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String sql = """
                -- Tabela de usuários
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT NOT NULL PRIMARY KEY,
                    password TEXT NOT NULL
                );

                -- Tabela de clientes
                CREATE TABLE IF NOT EXISTS clientes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    telefone TEXT,
                    cpf TEXT UNIQUE NOT NULL,
                    email TEXT,
                    cep TEXT,
                    logradouro TEXT,
                    numero TEXT,
                    complemento TEXT,
                    bairro TEXT,
                    localidade TEXT,
                    uf TEXT,
                    data_cadastro DATE NOT NULL
                );

                -- Tabela de fornecedores
                CREATE TABLE IF NOT EXISTS fornecedores (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    cnpj TEXT UNIQUE NOT NULL,
                    telefone TEXT,
                    email TEXT,
                    representante TEXT,
                    telefone_representante TEXT,
                    email_representante TEXT,
                    cep TEXT,
                    logradouro TEXT,
                    numero TEXT,
                    complemento TEXT,
                    bairro TEXT,
                    localidade TEXT,
                    uf TEXT,
                    data_cadastro DATE NOT NULL
                );

                -- Tabela de categorias (ESTRUTURA CORRIGIDA)
                CREATE TABLE IF NOT EXISTS categorias (
                    id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL UNIQUE,
                    descricao TEXT
                );

                -- Tabela de subcategorias
                CREATE TABLE IF NOT EXISTS subcategorias (
                    id_subcategoria INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    categoria_id INTEGER NOT NULL,
                    FOREIGN KEY (categoria_id) REFERENCES categorias(id_categoria),
                    UNIQUE (nome, categoria_id)
                );

                -- Tabela de produtos
                CREATE TABLE IF NOT EXISTS produtos (
                    id_produto INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome_produto TEXT NOT NULL,
                    quantidade INTEGER NOT NULL DEFAULT 0,
                    preco TEXT NOT NULL, -- Alterado de REAL para TEXT para armazenar o valor exato
                    status TEXT NOT NULL CHECK (status IN ('Estoque normal', 'Baixo Estoque', 'Esgotado')),
                    codigo_barras TEXT UNIQUE,
                    categoria_id INTEGER NOT NULL,
                    subcategoria_id INTEGER NOT NULL,
                    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (categoria_id) REFERENCES categorias(id_categoria),
                    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias(id_subcategoria)
                );

                    
                CREATE TABLE IF NOT EXISTS contas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    descricao TEXT NOT NULL,
                    valor REAL NOT NULL,
                    vencimento DATE NOT NULL,
                    pago BOOLEAN NOT NULL DEFAULT 0,
                    tipo TEXT NOT NULL CHECK (tipo IN ('PAGAR', 'RECEBER')),
                    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    data_pagamento DATE,
                    observacao TEXT
                 );
                 
                CREATE TABLE IF NOT EXISTS vendas (
                    data TEXT NOT NULL,                                                                                                                                  \s
                    formaPagamento TEXT,
                    status TEXT
                 );
                 
                CREATE TABLE IF NOT EXISTS caixa (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    data_abertura DATE NOT NULL,
                    data_fechamento DATE,
                    vendedor TEXT NOT NULL,
                    caixa_inicial REAL NOT NULL,
                    caixa_final REAL,
                    status TEXT NOT NULL CHECK (status IN ('ABERTO', 'FECHADO')),
                    observacoes TEXT
                 );
                 
                CREATE TABLE IF NOT EXISTS movimentacao_caixa (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_caixa INTEGER NOT NULL,
                    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    tipo TEXT NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
                    valor REAL NOT NULL,
                    forma_pagamento TEXT CHECK (forma_pagamento IN ('DINHEIRO', 'CARTAO', 'CHEQUE', 'OUTRO')),
                    descricao TEXT,
                    responsavel TEXT,
                    FOREIGN KEY (id_caixa) REFERENCES caixa(id)
                 );
                 
                CREATE TABLE IF NOT EXISTS itens_venda (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_venda INTEGER NOT NULL,
                    id_produto INTEGER NOT NULL,
                    quantidade INTEGER NOT NULL,
                    preco_unitario REAL NOT NULL,
                    FOREIGN KEY (id_venda) REFERENCES vendas(codigo),
                    FOREIGN KEY (id_produto) REFERENCES produtos(id_produto)
                 );
                 
                -- Índices para melhorar a performance nas consultas
                CREATE INDEX IF NOT EXISTS idx_caixa_status_data ON caixa(status, data_abertura);
                CREATE INDEX IF NOT EXISTS idx_movimentacao_caixa ON movimentacao_caixa(id_caixa, tipo);
                
                -- Inserir dados iniciais
                INSERT OR IGNORE INTO categorias (id_categoria, nome) VALUES 
                (1, 'ELETRONICOS'),
                (2, 'ALIMENTOS'),
                (3, 'ROUPAS'),
                (4, 'MOVEIS');

                INSERT OR IGNORE INTO subcategorias (id_subcategoria, nome, categoria_id) VALUES
                (1, 'CELULARES', 1),
                (2, 'NOTEBOOKS', 1),
                (3, 'FRUTAS', 2),
                (4, 'VEGETAIS', 2),
                (5, 'CAMISAS', 3),
                (6, 'CALCAS', 3),
                (7, 'SOFAS', 4),
                (8, 'MESAS', 4);
            """;

            stmt.executeUpdate(sql);
            System.out.println("Banco de dados inicializado com sucesso!");

        } catch (SQLException e) {
            System.err.println("ERRO CRÍTICO ao inicializar banco de dados:");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("Causa: " + e.getCause());
            e.printStackTrace();
            throw new RuntimeException("Falha ao inicializar banco de dados", e);
        }
    }
}