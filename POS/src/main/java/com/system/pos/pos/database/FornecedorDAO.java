package com.system.pos.pos.database;
import com.system.pos.pos.model.Fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FornecedorDAO {

    private Connection connection;

    public FornecedorDAO(){
        this.connection=ConnectionDB.conectar();
    }

    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO fornecedores (nome, cpf_cnpj, telefone, email, endereco, tipo, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,fornecedor.getNome());
            preparedStatement.setString(2,fornecedor.getCnpj());
            preparedStatement.setString(3,fornecedor.getTelefone());
            preparedStatement.setString(4,fornecedor.getEmail());
            preparedStatement.setString(5,fornecedor.getEndereco().toString());
            preparedStatement.executeUpdate();
        }
    }

    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException{

    }

    public void removerFornecedor(Fornecedor fornecedor) throws SQLException{

    }


    public Fornecedor exibirFornecedores() throws SQLException{


        return null;
    }


}
