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
                        password TEXT
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
