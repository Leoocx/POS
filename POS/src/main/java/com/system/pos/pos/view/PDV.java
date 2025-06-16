package com.system.pos.pos.view;

import com.system.pos.pos.exceptions.BusinessException;
import com.system.pos.pos.model.ItemVenda;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.model.Venda;
import com.system.pos.pos.service.ProdutoService;
import com.system.pos.pos.service.VendaService;
import com.system.pos.pos.utils.AlertUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import static com.system.pos.pos.utils.AlertUtil.mostrarAlerta;

public class PDV {

    @FXML private Label lblNumeroVenda;
    @FXML private Label lblDataVenda;
    @FXML private Label lblCliente;
    @FXML private Label lblVendedor;
    @FXML private Label lblTotalBruto;
    @FXML private Label lblQtdItens;
    @FXML private Label lblTotalLiquido;
    @FXML private Label lblTotalPago;
    @FXML private Label lblTroco;


    @FXML private TextField txtDescricao;
    @FXML private TextField txtID;
    @FXML private TextField txtValorUnitario;
    @FXML private TextField txtValorPgto1;
    @FXML private TextField txtQuantidade;

    @FXML private TableView<Produto> tableViewProdutos;
    @FXML private TableColumn<Produto, String> colDescricao;
    @FXML private TableColumn<Produto, Integer> colID;
    @FXML private TableColumn<Produto, BigDecimal> colValorUnitario;
    @FXML private TableView<ItemVenda> tableViewItensVenda;
    @FXML private TableColumn<ItemVenda, String> colItemDescricao;
    @FXML private TableColumn<ItemVenda, Integer> colItemQuantidade;
    @FXML private TableColumn<ItemVenda, String> colItemTotal;

    private final ObservableList<ItemVenda> itensVenda = FXCollections.observableArrayList();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private final ProdutoService produtoService = new ProdutoService();
    private final VendaService vendaService = new VendaService();
    private Venda vendaAtual;
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @FXML
    public void initialize() {
        configurarTabelas();
        carregarDadosIniciais();
        configurarTeclasAtalho();
        configurarListeners();
        vendaAtual = new Venda();

        tableViewProdutos.setRowFactory(tv -> {
            TableRow<Produto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    adicionarProdutoAVenda(row.getItem());
                }
            });
            return row;
        });

        txtQuantidade.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtQuantidade.setText(oldVal);
            }
        });

        txtValorPgto1.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*([.,]\\d{0,2})?")) {
                txtValorPgto1.setText(oldVal);
            }
            calcularTroco();
        });
    }

    private void configurarTabelas() {
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colDescricao.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colValorUnitario.setCellValueFactory(cellData -> cellData.getValue().precoProperty());

        colItemDescricao.setCellValueFactory(cellData -> cellData.getValue().getProduto().nomeProperty());
        colItemQuantidade.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty().asObject());
        colItemTotal.setCellValueFactory(cellData -> {
            BigDecimal total = cellData.getValue().getTotalItem();
            return new SimpleStringProperty(currencyFormat.format(total));
        });

        tableViewProdutos.setItems(produtoService.listarProdutos());
        tableViewItensVenda.setItems(itensVenda);
    }

    private void carregarDadosIniciais() {
        lblVendedor.setText("Operador 1");
        lblNumeroVenda.setText("001");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            String dataHoraAtual = LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                            lblDataVenda.setText(dataHoraAtual);
                        }
                ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        lblCliente.setText("CLIENTE PADRÃO");
        txtQuantidade.setText("1");
        resetarValoresMonetarios();
    }

    private void resetarValoresMonetarios() {
        lblTotalBruto.setText(currencyFormat.format(BigDecimal.ZERO));
        lblTotalLiquido.setText(currencyFormat.format(BigDecimal.ZERO));
        lblTotalPago.setText(currencyFormat.format(BigDecimal.ZERO));
        lblTroco.setText(currencyFormat.format(BigDecimal.ZERO));
        discountAmount = BigDecimal.ZERO;
    }

    private void configurarTeclasAtalho() {
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
            case F9 -> cancelarVenda();
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

    private void configurarListeners() {
        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                preencherCamposProduto(newVal);
            }
        });
    }

    private void preencherCamposProduto(Produto produto) {
        txtID.setText(String.valueOf(produto.getId()));
        txtDescricao.setText(produto.getNome());
        txtValorUnitario.setText(currencyFormat.format(produto.getPreco()));
        txtQuantidade.setText("1");
        txtQuantidade.requestFocus();
    }

    private void atualizarTotais() {
        BigDecimal totalBruto = calcularTotalBruto();
        int totalItens = calcularTotalItens();
        BigDecimal totalLiquido = calcularTotalLiquido(totalBruto);

        lblTotalBruto.setText(currencyFormat.format(totalBruto));
        lblQtdItens.setText(String.valueOf(totalItens));
        lblTotalLiquido.setText(currencyFormat.format(totalLiquido));

        atualizarPagamentoETroco(totalLiquido);
    }

    private BigDecimal calcularTotalBruto() {
        return itensVenda.stream()
                .map(ItemVenda::getTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int calcularTotalItens() {
        return itensVenda.stream()
                .mapToInt(ItemVenda::getQuantidade)
                .sum();
    }

    private BigDecimal calcularTotalLiquido(BigDecimal totalBruto) {
        return totalBruto.subtract(discountAmount).max(BigDecimal.ZERO);
    }

    private void atualizarPagamentoETroco(BigDecimal totalLiquido) {
        BigDecimal pago = parseCurrency(txtValorPgto1.getText());
        lblTotalPago.setText(currencyFormat.format(pago));

        if (pago.compareTo(BigDecimal.ZERO) == 0) {
            lblTroco.setText(currencyFormat.format(BigDecimal.ZERO));
        } else if (pago.compareTo(totalLiquido) >= 0) {
            lblTroco.setText(currencyFormat.format(pago.subtract(totalLiquido)));
        } else {
            lblTroco.setText("Insuficiente");
        }
    }

    public void adicionarProdutoAVenda(Produto produto) {
        try {
            if (produto == null) return;

            Optional<ItemVenda> itemExistente = itensVenda.stream()
                    .filter(item -> item.getIdProduto() == produto.getId())
                    .findFirst();

            if (itemExistente.isPresent()) {
                atualizarQuantidadeExistente(itemExistente.get(), produto);
            } else {
                adicionarNovoItem(produto);
            }
            atualizarTotais();
        } catch (BusinessException e) {
            mostrarAlerta("Erro", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void atualizarQuantidadeExistente(ItemVenda item, Produto produto) throws BusinessException {
        int novaQuantidade = item.getQuantidade() + 1;
        if (novaQuantidade > produto.getQuantidade()) {
            throw new BusinessException("Estoque insuficiente. Disponível: " + produto.getQuantidade());
        }
        item.setQuantidade(novaQuantidade);
        tableViewItensVenda.refresh();
    }

    private void adicionarNovoItem(Produto produto) throws BusinessException {
        if (produto.getQuantidade() < 1) {
            throw new BusinessException("Produto sem estoque disponível");
        }
        ItemVenda novoItem = new ItemVenda(produto, 1);
        itensVenda.add(novoItem);
        vendaAtual.adicionarItem(novoItem);
    }

    private BigDecimal parseCurrency(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            // Remove todos os não-numéricos exceto vírgula/ponto
            String cleanValue = value.replaceAll("[^\\d,.]", "")
                    .replace(".", "")
                    .replace(",", ".");
            return new BigDecimal(cleanValue);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    @FXML
    private void calcularTroco() {
        try {
            BigDecimal total = parseCurrency(lblTotalLiquido.getText());
            BigDecimal pago = parseCurrency(txtValorPgto1.getText());

            lblTotalPago.setText(currencyFormat.format(pago));

            // Calcula o troco
            if (pago.compareTo(BigDecimal.ZERO) == 0) {
                lblTroco.setText(currencyFormat.format(BigDecimal.ZERO));
            } else if (pago.compareTo(total) >= 0) {
                BigDecimal troco = pago.subtract(total);
                lblTroco.setText(currencyFormat.format(troco));
            } else {
                lblTroco.setText("Insuficiente");
            }
        } catch (Exception e) {
            lblTroco.setText("Inválido");
            e.printStackTrace(); // apenas para debug
        }
    }

    private void cancelarVenda() {
        itensVenda.clear();
        vendaAtual = new Venda();
        txtValorPgto1.clear();
        resetarValoresMonetarios();
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

    private void adicionarItem() {
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) {
            mostrarAlerta("Aviso", "Nenhum produto selecionado.", Alert.AlertType.INFORMATION);
            return;
        }

        try {
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            if (quantidade <= 0) {
                mostrarAlerta("Erro", "A quantidade deve ser maior que zero.", Alert.AlertType.INFORMATION);
                return;
            }

            if (quantidade > produto.getQuantidade()) {
                mostrarAlerta("Estoque insuficiente", "Quantidade disponível: " + produto.getQuantidade(), Alert.AlertType.INFORMATION);
                return;
            }

            Optional<ItemVenda> itemExistente = itensVenda.stream()
                    .filter(item -> item.getIdProduto() == produto.getId())
                    .findFirst();

            if (itemExistente.isPresent()) {
                ItemVenda item = itemExistente.get();
                item.setQuantidade(item.getQuantidade() + quantidade);
            } else {
                ItemVenda novoItem = new ItemVenda(produto, quantidade);
                itensVenda.add(novoItem);
            }

            atualizarTotais();
            limparCamposProduto();
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Quantidade inválida.", Alert.AlertType.INFORMATION);
        }
    }

    private void limparCamposProduto() {
        txtID.clear();
        txtDescricao.clear();
        txtValorUnitario.clear();
        txtQuantidade.setText("1");
    }

    @FXML
    private void aplicarDesconto() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Desconto");
        dialog.setHeaderText("Informe o valor ou percentual");
        dialog.setContentText("Ex: 10 ou 10%");

        dialog.showAndWait().ifPresent(desconto -> {
            try {
                BigDecimal total = calcularTotalBruto();
                BigDecimal valorDesconto;

                String input = desconto.replace(",", ".").replace("%", "");

                if (input.startsWith("-")) {
                    mostrarAlerta("Erro", "O desconto não pode ser negativo.", Alert.AlertType.INFORMATION);
                    return;
                }

                if (desconto.contains("%")) {
                    double percentual = Double.parseDouble(input) / 100;
                    valorDesconto = total.multiply(BigDecimal.valueOf(percentual));
                } else {
                    valorDesconto = new BigDecimal(input);
                }

                if (valorDesconto.compareTo(total) > 0) {
                    mostrarAlerta("Aviso", "Desconto maior que o total da venda. Ajustado para o valor total.", Alert.AlertType.INFORMATION);
                    valorDesconto = total;
                }

                discountAmount = valorDesconto;
                atualizarTotais();

            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Valor inválido. Use números (ex: 10 ou 10.5%)", Alert.AlertType.INFORMATION);
            }
        });
    }

    @FXML
    private void novaVenda() {
        vendaAtual = new Venda();
        itensVenda.clear();
        txtValorPgto1.clear();
        resetarValoresMonetarios();
        lblNumeroVenda.setText(String.format("%03d", Integer.parseInt(lblNumeroVenda.getText()) + 1));
        lblDataVenda.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }

    @FXML
    private void finalizarVenda() {
        if (itensVenda.isEmpty()) {
            mostrarAlerta("Aviso", "Adicione itens à venda", Alert.AlertType.INFORMATION);
            return;
        }

        BigDecimal totalLiquido = parseCurrency(lblTotalLiquido.getText());
        BigDecimal valorPago = parseCurrency(txtValorPgto1.getText());

        if (valorPago.compareTo(totalLiquido) < 0) {
            mostrarAlerta("Aviso", "Valor pago é menor que o total", Alert.AlertType.INFORMATION);
            return;
        }

        boolean sucesso = vendaService.registrarVenda(itensVenda, "DINHEIRO");

        if (sucesso) {
            String comprovante = gerarComprovanteVenda();
            exibirComprovante(comprovante);
            mostrarAlerta("Sucesso", "Venda registrada", Alert.AlertType.INFORMATION);
            novaVenda();
        } else {
            mostrarAlerta("Erro", "Falha ao registrar venda", Alert.AlertType.INFORMATION);
        }
    }

    private void exibirComprovante(String comprovante) {
        TextArea textArea = new TextArea(comprovante);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: monospace;");

        Button btnImprimir = new Button("Imprimir");
        btnImprimir.setOnAction(e -> imprimirComprovante(comprovante));

        VBox root = new VBox(10, textArea, btnImprimir);
        root.setPadding(new Insets(15));

        Stage stage = new Stage();
        stage.setTitle("Comprovante de Venda");
        stage.setScene(new Scene(root, 400, 500));
        stage.show();
    }

    private void imprimirComprovante(String comprovante) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            Text printerText = new Text(comprovante);
            printerText.setFont(Font.font("Monospaced", 10));
            printerText.setTextAlignment(TextAlignment.LEFT);

            StackPane printRoot = new StackPane(printerText);
            printRoot.setPadding(new Insets(20));

            boolean success = job.printPage(printRoot);
            if (success) {
                job.endJob();
            } else {
                mostrarAlerta("Erro", "Falha ao imprimir comprovante.", Alert.AlertType.INFORMATION);
            }
        }
    }

    private String gerarComprovanteVenda() {
        StringBuilder comprovante = new StringBuilder();

        comprovante.append("----------------------------------------\n");
        comprovante.append("          COMPROVANTE DE VENDA          \n");
        comprovante.append("----------------------------------------\n");
        comprovante.append("Número: ").append(lblNumeroVenda.getText()).append("\n");
        comprovante.append("Data: ").append(lblDataVenda.getText()).append("\n");
        comprovante.append("Vendedor: ").append(lblVendedor.getText()).append("\n");
        comprovante.append("Cliente: ").append(lblCliente.getText()).append("\n");
        comprovante.append("----------------------------------------\n");
        comprovante.append("ITENS:\n");

        for (ItemVenda item : itensVenda) {
            comprovante.append(String.format(
                    "%-20s %3d x %-8s %10s\n",
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    currencyFormat.format(item.getPrecoUnitario()),
                    currencyFormat.format(item.getTotalItem())
            ));
        }

        comprovante.append("----------------------------------------\n");
        comprovante.append(String.format("%-30s %10s\n", "TOTAL BRUTO:", lblTotalBruto.getText()));
        comprovante.append(String.format("%-30s %10s\n", "TOTAL LÍQUIDO:", lblTotalLiquido.getText()));
        comprovante.append(String.format("%-30s %10s\n", "VALOR PAGO:", lblTotalPago.getText()));
        comprovante.append(String.format("%-30s %10s\n", "TROCO:", lblTroco.getText()));
        comprovante.append("----------------------------------------\n");
        comprovante.append("Obrigado pela preferência!\n");

        return comprovante.toString();
    }
}