package com.system.pos.pos.view;

import com.system.pos.pos.model.ItemVenda;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.service.ProdutoService;
import com.system.pos.pos.service.VendaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CaixaView {
    // Labels
    @FXML private Label lblNumeroVenda;
    @FXML private Label lblDataVenda;
    @FXML private Label lblCliente;
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
    @FXML private TextField txtQuantidade;

    // Tabelas
    @FXML private TableView<Produto> tableViewProdutos;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, Integer> colID;
    @FXML private TableColumn<Produto, Double> colValorUnitario;
    @FXML private TableView<ItemVenda> tableViewItensVenda;
    @FXML private TableColumn<ItemVenda, String> colItemDescricao;
    @FXML private TableColumn<ItemVenda, Integer> colItemQuantidade;
    @FXML private TableColumn<ItemVenda, String> colItemTotal;

    private final ObservableList<ItemVenda> itensVenda = FXCollections.observableArrayList();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private final ProdutoService produtoService = new ProdutoService();
    private final VendaService vendaService = new VendaService();

    @FXML
    public void initialize() {
        configurarTabelas();
        carregarDadosIniciais();
        configurarTeclasAtalho();
        configurarListeners();
    }

    private void configurarTabelas() {
        // Tabela de produtos
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colDescricao.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colValorUnitario.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        // Tabela de itens da venda
        colItemDescricao.setCellValueFactory(cellData -> cellData.getValue().nomeProdutoProperty());
        colItemQuantidade.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty().asObject());
        colItemTotal.setCellValueFactory(cellData -> {
            double total = cellData.getValue().getPrecoUnitario() * cellData.getValue().getQuantidade();
            return new SimpleStringProperty(currencyFormat.format(total));
        });

        tableViewProdutos.setItems(produtoService.listarProdutos());
        tableViewItensVenda.setItems(itensVenda);
    }

    private void configurarListeners() {
        // Preenche automaticamente os campos ao selecionar um produto
        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                preencherCamposProduto(newVal);
            }
        });

        // Calcula troco automaticamente ao alterar valor pago
        txtValorPgto1.textProperty().addListener((obs, oldVal, newVal) -> {
            calcularTroco();
        });
    }

    private void preencherCamposProduto(Produto produto) {
        txtID.setText(String.valueOf(produto.getId()));
        txtDescricao.setText(produto.getNome());
        txtValorUnitario.setText(currencyFormat.format(produto.getPreco()));
        txtQuantidade.setText("1");
        txtQuantidade.requestFocus();
    }

    private void carregarDadosIniciais() {
        lblVendedor.setText("Operador 1");
        lblNumeroVenda.setText("001");
        lblDataVenda.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        lblCliente.setText("CLIENTE PADRÃO");
        txtQuantidade.setText("1");
        lblTotalBruto.setText("R$ 0,00");
        lblTotalLiquido.setText("R$ 0,00");
        lblTotalPago.setText("R$ 0,00");
        lblTroco.setText("R$ 0,00");
    }

    private void configurarTeclasAtalho() {
        // Configura teclas de atalho quando a cena estiver disponível
        tableViewProdutos.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(this::tratarTeclasAtalho);
            }
        });
    }

    private void tratarTeclasAtalho(KeyEvent event) {
        switch (event.getCode()) {
            case F1 -> aplicarDesconto();
            case F2 -> novaVenda();
            case F3 -> buscarProduto();
            case ENTER -> {
                if (event.getTarget() instanceof TextField) {
                    tratarEnter((TextField) event.getTarget());
                }
            }
            case F10 -> finalizarVenda();
            case ADD, PLUS -> aumentarQuantidade();
            case SUBTRACT, MINUS -> diminuirQuantidade();
        }
    }

    private void tratarEnter(TextField campo) {
        if (campo == txtDescricao || campo == txtID) {
            buscarProduto();
        } else if (campo == txtQuantidade) {
            adicionarItem();
        }
    }

    private void aumentarQuantidade() {
        try {
            int qtd = Integer.parseInt(txtQuantidade.getText());
            txtQuantidade.setText(String.valueOf(qtd + 1));
        } catch (NumberFormatException e) {
            txtQuantidade.setText("1");
        }
    }

    private void diminuirQuantidade() {
        try {
            int qtd = Integer.parseInt(txtQuantidade.getText());
            if (qtd > 1) {
                txtQuantidade.setText(String.valueOf(qtd - 1));
            }
        } catch (NumberFormatException e) {
            txtQuantidade.setText("1");
        }
    }

    @FXML
    private void buscarProduto() {
        String busca = txtDescricao.getText().isEmpty() ? txtID.getText() : txtDescricao.getText();
        tableViewProdutos.setItems(produtoService.buscarProdutos(busca));

        if (!tableViewProdutos.getItems().isEmpty()) {
            tableViewProdutos.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void adicionarItem() {
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) {
            mostrarAlerta("Aviso", "Nenhum produto selecionado.");
            return;
        }

        try {
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            if (quantidade <= 0) {
                mostrarAlerta("Erro", "A quantidade deve ser maior que zero.");
                return;
            }

            if (quantidade > produto.getQuantidade()) {
                mostrarAlerta("Estoque insuficiente", "Quantidade disponível: " + produto.getQuantidade());
                return;
            }

            // Verifica se já existe um item com o mesmo produto na venda
            ItemVenda itemExistente = itensVenda.stream()
                    .filter(item -> item.getIdProduto() == produto.getId())
                    .findFirst()
                    .orElse(null);

            if (itemExistente != null) {
                int novaQuantidade = itemExistente.getQuantidade() + quantidade;
                if (novaQuantidade > produto.getQuantidade()) {
                    mostrarAlerta("Estoque insuficiente", "Quantidade disponível: " + produto.getQuantidade());
                    return;
                }
                itemExistente.setQuantidade(novaQuantidade);
            } else {
                ItemVenda novoItem = new ItemVenda();
                novoItem.setIdProduto(produto.getId());
                novoItem.setPrecoUnitario(produto.getPreco());
                novoItem.setQuantidade(quantidade);
                itensVenda.add(novoItem);
            }

            // Simula a retirada do estoque local durante a venda
            produto.setQuantidade(produto.getQuantidade() - quantidade);

            atualizarTotais();
            limparCamposProduto();

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Quantidade inválida.");
        }
    }

    private void atualizarTotais() {
        BigDecimal totalBruto = BigDecimal.ZERO;
        int totalItens = 0;

        for (ItemVenda item : itensVenda) {
            BigDecimal totalItem = BigDecimal.valueOf(item.getPrecoUnitario()).multiply(BigDecimal.valueOf(item.getQuantidade()));
            totalBruto = totalBruto.add(totalItem);
            totalItens += item.getQuantidade();
        }

        lblTotalBruto.setText(currencyFormat.format(totalBruto));
        lblTotalLiquido.setText(currencyFormat.format(totalBruto)); // Considerando sem desconto
        lblQtdItens.setText(String.valueOf(totalItens));
        lblTotalPago.setText(txtValorPgto1.getText().isEmpty() ? "R$ 0,00" : currencyFormat.format(new BigDecimal(txtValorPgto1.getText().replace(",", "."))));
        calcularTroco();
    }

    private void limparCamposProduto() {
        txtID.clear();
        txtDescricao.clear();
        txtValorUnitario.clear();
        txtQuantidade.setText("1");
    }

    private BigDecimal calcularTotalVenda() {
        return itensVenda.stream()
                .map(item -> BigDecimal.valueOf(item.getPrecoUnitario() * item.getQuantidade()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @FXML
    private void aplicarDesconto() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Desconto");
        dialog.setHeaderText("Informe o valor ou percentual");
        dialog.setContentText("Ex: 10 ou 10%");

        dialog.showAndWait().ifPresent(desconto -> {
            try {
                BigDecimal total = calcularTotalVenda();
                BigDecimal valorDesconto;

                if (desconto.contains("%")) {
                    double percentual = Double.parseDouble(desconto.replace("%", "")) / 100;
                    valorDesconto = total.multiply(BigDecimal.valueOf(percentual));
                } else {
                    valorDesconto = new BigDecimal(desconto);
                }

                lblTotalLiquido.setText(currencyFormat.format(total.subtract(valorDesconto)));
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Valor inválido");
            }
        });
    }

    @FXML
    private void novaVenda() {
        itensVenda.clear();
        txtValorPgto1.clear();
        atualizarTotais();
        lblNumeroVenda.setText(String.format("%03d", Integer.parseInt(lblNumeroVenda.getText()) + 1));
        lblDataVenda.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }

    @FXML
    private void calcularTroco() {
        try {
            BigDecimal total = new BigDecimal(lblTotalLiquido.getText().replaceAll("[^\\d.,]", "").replace(",", "."));
            BigDecimal pago = txtValorPgto1.getText().isEmpty() ? BigDecimal.ZERO
                    : new BigDecimal(txtValorPgto1.getText().replace(",", "."));

            lblTotalPago.setText(currencyFormat.format(pago));

            if (pago.compareTo(total) >= 0) {
                lblTroco.setText(currencyFormat.format(pago.subtract(total)));
            } else {
                lblTroco.setText("Insuficiente");
            }
        } catch (NumberFormatException e) {
            lblTroco.setText("Inválido");
        }
    }

    @FXML
    private void finalizarVenda() {
        if (itensVenda.isEmpty()) {
            mostrarAlerta("Aviso", "Adicione itens à venda");
            return;
        }

        calcularTroco();

        if (lblTroco.getText().equals("Insuficiente")) {
            mostrarAlerta("Aviso", "Valor pago é menor que o total");
            return;
        }

        boolean sucesso = vendaService.registrarVenda(itensVenda, "DINHEIRO");

        if (sucesso) {
            mostrarAlerta("Sucesso", "Venda registrada");
            novaVenda();
        } else {
            mostrarAlerta("Erro", "Falha ao registrar venda");
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}