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
        cpf_cnpj TEXT UNIQUE NOT NULL,
        telefone TEXT,
        email TEXT,
        endereco TEXT
    );
    
    CREATE TABLE IF NOT EXISTS produtos (
        id_produto INTEGER PRIMARY KEY,
        nome_produto TEXT NOT NULL,
        quantidade INTEGER,
        preco REAL,
        status TEXT
    );
    
    CREATE TABLE IF NOT EXISTS vendas (
        codigo INTEGER PRIMARY KEY,
        quantidade INTEGER NOT NULL,
        precoUnitario REAL NOT NULL,
        data TEXT NOT NULL,
        cliente INTEGER,
        formaPagamento TEXT,
        desconto REAL,
        status TEXT,
        FOREIGN KEY (cliente) REFERENCES clientes(codigo)
    );
""";


                stmt.executeUpdate(sql);
                System.out.println("Tabelas criadas ou já existentes.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public static void main(String[] args){
        criarTabelas();
    }
}
