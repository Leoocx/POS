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
                        rs.getString("codigo"),                     // Código do produto
                        rs.getInt("estoque_atual"),                 // Estoque atual
                        rs.getString("localizacao"),                // Localização (Adicionado)
                        rs.getDouble("comissao"),                   // Comissão (Adicionado)
                        rs.getDate("validade") != null ? rs.getDate("validade").toLocalDate() : null, // Validade (Adicionado e tratado para evitar erro)
                        rs.getString("marca"),                      // Marca (Adicionado)
                        rs.getDouble("preco_compra"),               // Preço de compra
                        rs.getString("descricao"),                  // Descrição do produto
                        new Fornecedor(rs.getInt("fornecedor_id")), // Fornecedor (Criando objeto, supondo que só tem ID no banco)
                        rs.getDouble("preco_venda"),                // Preço de venda
                        rs.getString("unidade"),                    // Unidade de medida (Adicionado)
                        rs.getString("codigo_barras"),              // Código de barras (Adicionado)
                        rs.getDouble("lucro"),                      // Lucro (Adicionado)
                        new SubCategoria(rs.getInt()), // Subcategoria (Criando objeto com ID)
                        new Categoria(rs.getInt("categoria_id")),   // Categoria (Criando objeto com ID)
                        rs.getInt("garantia"),                      // Garantia (Adicionado)
                        rs.getString("referencia")                  // Referência do produto (Adicionado)
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
