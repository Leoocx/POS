import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FornecedorDAO {

    private Connection connection;

    public FornecedorDAO(Connection connection){
        this.connection=connection;
    }

    public void adicionarFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO Fornecedor (nome, cpf_cnpj, telefone, email, endereco, tipo, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,fornecedor.getNome());
            preparedStatement.setString(2,fornecedor.getCnpj());
            preparedStatement.setString(3,fornecedor.getTelefone());
            preparedStatement.setString(4,fornecedor.getEmail());
            preparedStatement.setString(5,fornecedor.getEndereco().toString());
            preparedStatement.executeUpdate();
        }
    }

}
