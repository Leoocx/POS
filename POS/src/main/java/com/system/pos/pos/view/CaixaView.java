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
import java.util.List;
import java.util.Locale;

public class CaixaView {
    @FXML private Label lblVendedor;
    @FXML private Label lblNumeroCaixa;
    @FXML private TextField txtBuscaProduto;
    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TextField txtQuantidade;
    @FXML private TableView<ItemVenda> tabelaItens;
    @FXML private Label lblTotalVenda;
    @FXML private ComboBox<String> cbFormaPagamento;
    @FXML private TableColumn<ItemVenda, String> colunaRemover;
    private final ProdutoService produtoService = new ProdutoService();
    private final VendaService vendaService = new VendaService();
    private final ObservableList<ItemVenda> itensVenda = FXCollections.observableArrayList();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    public void initialize() {
        configurarTabelas();
        configurarComboBox();
        carregarDadosIniciais();
        colunaRemover = new TableColumn<>();
        colunaRemover.setCellFactory(col -> new TableCell<ItemVenda, String>() {
            private final Button btn = new Button("X");

            {
                btn.setStyle("-fx-base: #F44336;");
                btn.setOnAction(event -> {
                    removerItem();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void configurarTabelas() {
        // Configura colunas da tabela de produtos
        TableColumn<Produto, Integer> colId = new TableColumn<>("Código");
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Produto, String> colNome = new TableColumn<>("Produto");
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());

        TableColumn<Produto, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        TableColumn<Produto, Integer> colEstoque = new TableColumn<>("Estoque");
        colEstoque.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty().asObject());

        tabelaProdutos.getColumns().addAll(colId, colNome, colPreco, colEstoque);
        tabelaProdutos.setItems(produtoService.listarProdutos());

        // Configura colunas da tabela de itens
        TableColumn<ItemVenda, Integer> colIdItem = new TableColumn<>("Código");
        colIdItem.setCellValueFactory(cellData -> cellData.getValue().idProdutoProperty().asObject());

        TableColumn<ItemVenda, String> colNomeItem = new TableColumn<>("Produto");
        colNomeItem.setCellValueFactory(cellData -> cellData.getValue().nomeProdutoProperty());

        TableColumn<ItemVenda, Integer> colQtdItem = new TableColumn<>("Qtd");
        colQtdItem.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty().asObject());

        TableColumn<ItemVenda, Double> colPrecoItem = new TableColumn<>("Preço");
        colPrecoItem.setCellValueFactory(cellData -> cellData.getValue().precoUnitarioProperty().asObject());

        TableColumn<ItemVenda, BigDecimal> colTotalItem = new TableColumn<>("Total");
        colTotalItem.setCellValueFactory(cellData -> cellData.getValue().totalItemProperty());

        tabelaItens.getColumns().addAll(colIdItem, colNomeItem, colQtdItem, colPrecoItem, colTotalItem);
        tabelaItens.setItems(itensVenda);
    }

    private void configurarComboBox() {
        cbFormaPagamento.getItems().addAll("DINHEIRO", "CARTAO", "CHEQUE");
    }

    private void carregarDadosIniciais() {
        lblVendedor.setText("Teste");
        lblNumeroCaixa.setText("001");
    }

    @FXML
    private void buscarProduto() {
        String busca = txtBuscaProduto.getText();
        tabelaProdutos.setItems(produtoService.buscarProdutos(busca));
    }

    @FXML
    private void adicionarItem() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

        if (produtoSelecionado != null) {
            try {
                int quantidade = Integer.parseInt(txtQuantidade.getText());

                if (quantidade <= 0) {
                    mostrarAlerta("Quantidade inválida", "A quantidade deve ser maior que zero.");
                    return;
                }

                if (quantidade > produtoSelecionado.getQuantidade()) {
                    mostrarAlerta("Estoque insuficiente", "Quantidade em estoque: " + produtoSelecionado.getQuantidade());
                    return;
                }

                ItemVenda item = new ItemVenda();
                item.setIdProduto(produtoSelecionado.getId());
                item.setNomeProduto(produtoSelecionado.getNome());
                item.setPrecoUnitario(produtoSelecionado.getPreco());
                item.setQuantidade(quantidade);

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
        ItemVenda itemSelecionado = tabelaItens.getSelectionModel().getSelectedItem();

        if (itemSelecionado != null) {
            itensVenda.remove(itemSelecionado);
            atualizarTotalVenda();
        }
    }

    @FXML
    private void finalizarVenda() {
        if (itensVenda.isEmpty()) {
            mostrarAlerta("Venda vazia", "Adicione itens à venda antes de finalizar.");
            return;
        }

        if (cbFormaPagamento.getSelectionModel().isEmpty()) {
            mostrarAlerta("Forma de pagamento", "Selecione a forma de pagamento.");
            return;
        }

        String formaPagamento = cbFormaPagamento.getValue();
        BigDecimal totalVenda = calcularTotalVenda();

        boolean sucesso = vendaService.registrarVenda(itensVenda, formaPagamento);

        if (sucesso) {
            mostrarAlerta("Sucesso", "Venda registrada com sucesso!");
            itensVenda.clear();
            atualizarTotalVenda();
            cbFormaPagamento.getSelectionModel().clearSelection();
        } else {
            mostrarAlerta("Erro", "Ocorreu um erro ao registrar a venda.");
        }
    }



    private void atualizarTotalVenda() {
        BigDecimal total = calcularTotalVenda();
        lblTotalVenda.setText(currencyFormat.format(total));
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