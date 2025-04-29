package com.system.pos.pos.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.system.pos.pos.database.ConnectionDB;

public class LoginService {

    private final Connection connection;

    public LoginService() {
        this.connection = ConnectionDB.conectar();
    }

    public void registrarConta(String username, String password) throws SQLException{
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        }
    }

    public boolean validarLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
