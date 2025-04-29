package com.system.pos.pos.database;
import com.system.pos.pos.model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VendaDAO {
    private Connection connection;
    public VendaDAO(){
        this.connection=ConnectionDB.conectar();
    }
    
    public void adicionarVenda(Venda venda) throws SQLException {
        String sql = "INSERT INTO vendas (codigo, quantidade, precoUnitario, data, cliente, formaPagamento, desconto, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(0, venda.getCodigo());
            stmt.setInt(1, venda.getQuantidade());
            stmt.setInt(2, venda.getPrecoUnitario());
            stmt.setString(3, venda.getData().toString());
            stmt.setString(4, venda.getCliente().toString());
            stmt.setString(5, venda.getFormaPagamento().toString());
            stmt.setFloat(6, venda.getDesconto());
            stmt.setString(7, venda.getStatus().toString());
            stmt.executeUpdate();
        }
    }

}
