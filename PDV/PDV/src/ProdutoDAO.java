import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection conexao;

    public ProdutoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void adicionarProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produto (codigo, descricao, preco_compra, preco_venda, categoria, estoque_atual) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoCompra());
            stmt.setDouble(4, produto.getPrecoVenda());
            stmt.setString(5, String.valueOf(produto.getCategoria()));
            stmt.setInt(6, produto.getEstoqueAtual());
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto";
        try (Statement stmt = conexao.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto p = new Produto(
                        rs.getString("codigo"),                     
                        rs.getInt("estoque_atual"),                 
                        rs.getString("localizacao"),               
                        rs.getDouble("comissao"),                   
                        rs.getDate("validade") != null ? rs.getDate("validade").toLocalDate() : null, 
                        rs.getString("marca"),                     
                        rs.getDouble("preco_compra"),               
                        rs.getString("descricao"),                  
                        new Fornecedor(rs.getInt("fornecedor_id")), 
                        rs.getDouble("preco_venda"),                
                        rs.getString("unidade"),                    
                        rs.getString("codigo_barras"),              
                        rs.getDouble("lucro"),                      
                        new SubCategoria(rs.getInt()), 
                        new Categoria(rs.getInt("categoria_id")),   
                        rs.getInt("garantia"),                      
                        rs.getString("referencia")                  
                );
                produtos.add(p);
            }
        }
        return produtos;
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE Produto SET descricao = ?, preco_compra = ?, preco_venda = ?, categoria = ?, estoque_atual = ? WHERE codigo = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, produto.getDescricao());
            stmt.setDouble(2, produto.getPrecoCompra());
            stmt.setDouble(3, produto.getPrecoVenda());
            new Categoria(4, produto.getCategoria());
            stmt.setInt(5, produto.getEstoqueAtual());
            stmt.setInt(6, produto.getCodigo());
            stmt.executeUpdate();
        }
    }

    public void removerProduto(int id) throws SQLException {
        String sql = "DELETE FROM Produto WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
