package com.system.pos.pos.database;
import com.system.pos.pos.model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VendaDAO {
    private Connection connection;
    public VendaDAO(Connection connection){
        this.connection=connection;
    }

    public void adicionarVenda(Venda venda) throws SQLException {
        String sql = "INSERT INTO Vendas (nome, cpf_cnpj, telefone, email, endereco, tipo, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){




        }

    }



}
