package com.system.pos.pos.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitialize {
    private static final String URL = "jdbc:sqlite:database.db"; 

    public static void criarTabelas() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                String sql = """
    -- Tabelas base corrigidas para banco SQLite
                        
                                                                                                                                            CREATE TABLE IF NOT EXISTS users (
                                                                                                                                                username TEXT NOT NULL,
                                                                                                                                                password TEXT NOT NULL,
                                                                                                                                                PRIMARY KEY (username)
                                                                                                                                            );
                        
                                                                                                                                            CREATE TABLE IF NOT EXISTS clientes (
                                                                                                                                                id INTEGER PRIMARY KEY,
                                                                                                                                                nome TEXT NOT NULL,
                                                                                                                                                telefone TEXT,
                                                                                                                                                cpf TEXT UNIQUE NOT NULL,
                                                                                                                                                email TEXT,
                                                                                                                                                endereco TEXT
                                                                                                                                            );
                        
                                                                                                                                            CREATE TABLE IF NOT EXISTS fornecedores (
                                                                                                                                                id INTEGER PRIMARY KEY,
                                                                                                                                                nome TEXT NOT NULL,
                                                                                                                                                cnpj TEXT UNIQUE NOT NULL,
                                                                                                                                                telefone TEXT,
                                                                                                                                                email TEXT
                                                                                                                                            );
                        
                                                                                                                                            CREATE TABLE IF NOT EXISTS produtos (
                                                                                                                                                id_produto INTEGER PRIMARY KEY,
                                                                                                                                                nome_produto TEXT NOT NULL,
                                                                                                                                                quantidade INTEGER,
                                                                                                                                                preco REAL,
                                                                                                                                                status TEXT
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
                                                                                                                                                data TEXT NOT NULL,                                                                                                                                   
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
                        
                        
""";



                stmt.executeUpdate(sql);
                System.out.println("Tabelas criadas ou já existentes.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}
