package com.system.pos.pos.view;

import com.system.pos.pos.model.ItemVenda;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.service.ProdutoService;
import com.system.pos.pos.service.VendaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CaixaView {
    // Labels
    @FXML private Label lblNumeroVenda;
    @FXML private Label lblDataVenda;
    @FXML private Label lblCliente;
    @FXML private Label lblHora;
    @FXML private Label lblVendedor;
    @FXML private Label lblTotalBruto;
    @FXML private Label lblQtdItens;
    @FXML private Label lblTotalLiquido;
    @FXML private Label lblTotalPago;
    @FXML private Label lblTroco;

    // Campos de texto
    @FXML private TextField txtDescricao;
    @FXML private TextField txtID;
    @FXML private TextField txtValorUnitario;
    @FXML private TextField txtValorPgto1;
    @FXML private TextField txtValorPgto2;

    // Tabela
    @FXML private TableView<Produto> tableViewProdutos;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, Integer> colID;
    @FXML private TableColumn<Produto, Double> colValorUnitario;

    // Combobox
    @FXML private ComboBox<String> cbFormaPgto1;
    @FXML private ComboBox<String> cbFormaPgto2;

    private final ProdutoService produtoService = new ProdutoService();
    private final VendaService vendaService = new VendaService();
    private final ObservableList<ItemVenda> itensVenda = FXCollections.observableArrayList();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    public void initialize() {
        configurarTabelas();
        configurarComboBox();
        carregarDadosIniciais();
    }

    private void configurarTabelas() {
        // Configura colunas da tabela de produtos
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colDescricao.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colValorUnitario.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        tableViewProdutos.setItems(produtoService.listarProdutos());
    }

    private void configurarComboBox() {
        cbFormaPgto1.getItems().addAll("DINHEIRO", "CARTAO", "CHEQUE");
        cbFormaPgto2.getItems().addAll("DINHEIRO", "CARTAO", "CHEQUE");
    }

    private void carregarDadosIniciais() {
        lblVendedor.setText("Teste");
        lblNumeroVenda.setText("001");
        lblDataVenda.setText("01/01/2023");
    }

    @FXML
    private void buscarProduto() {
        String busca = txtID.getText();
        tableViewProdutos.setItems(produtoService.buscarProdutos(busca));
    }

    @FXML
    private void adicionarItem() {
        Produto produtoSelecionado = tableViewProdutos.getSelectionModel().getSelectedItem();

        if (produtoSelecionado != null) {
            try {
                int quantidade = Integer.parseInt(lblQtdItens.getText());

                if (quantidade <= 0) {
                    mostrarAlerta("Quantidade inválida", "A quantidade deve ser maior que zero.");
                    return;
                }

                if (quantidade > produtoSelecionado.getQuantidade()) {
                    mostrarAlerta("Estoque insuficiente", "Quantidade em estoque: " + produtoSelecionado.getQuantidade());
                    return;
                }

                // Cria um novo item de venda
                ItemVenda item = new ItemVenda();
                item.setIdProduto(produtoSelecionado.getId());
                item.setNomeProduto(produtoSelecionado.getNome());
                item.setPrecoUnitario(produtoSelecionado.getPreco());
                item.setQuantidade(quantidade);

                // Adiciona o item à lista de itens de venda
                itensVenda.add(item);
                atualizarTotalVenda();

            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Quantidade inválida. Digite um número inteiro.");
            }
        } else {
            mostrarAlerta("Seleção inválida", "Selecione um produto para adicionar.");
        }
    }

    @FXML
    private void removerItem() {
        Produto itemSelecionado = tableViewProdutos.getSelectionModel().getSelectedItem();

        if (itemSelecionado != null) {
            itensVenda.remove(itemSelecionado);
            atualizarTotalVenda();
        } else {
            mostrarAlerta("Seleção inválida", "Selecione um item para remover.");
        }
    }

    @FXML
    private void finalizarVenda() {
        if (itensVenda.isEmpty()) {
            mostrarAlerta("Venda vazia", "Adicione itens à venda antes de finalizar.");
            return;
        }

        if (cbFormaPgto1.getSelectionModel().isEmpty()) {
            mostrarAlerta("Forma de pagamento", "Selecione a forma de pagamento.");
            return;
        }

        String formaPagamento = cbFormaPgto1.getValue();
        BigDecimal totalVenda = calcularTotalVenda();

        boolean sucesso = vendaService.registrarVenda(itensVenda, formaPagamento);

        if (sucesso) {
            mostrarAlerta("Sucesso", "Venda registrada com sucesso!");
            itensVenda.clear();
            atualizarTotalVenda();
            cbFormaPgto1.getSelectionModel().clearSelection();
        } else {
            mostrarAlerta("Erro", "Ocorreu um erro ao registrar a venda.");
        }
    }

    private void atualizarTotalVenda() {
        BigDecimal total = calcularTotalVenda();
        lblTotalBruto.setText(currencyFormat.format(total));
    }

    private BigDecimal calcularTotalVenda() {
        return itensVenda.stream()
                .map(item -> BigDecimal.valueOf(item.getPrecoUnitario()).multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}