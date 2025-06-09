package com.system.pos.pos.view;

import com.system.pos.pos.model.ItemVenda;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.service.ProdutoService;
import com.system.pos.pos.service.VendaService;
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

public class PDV {
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

    // New field to track applied discount
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @FXML
    public void initialize() {
        configurarTabelas();
        carregarDadosIniciais();
        configurarTeclasAtalho();
        configurarListeners();
        tableViewProdutos.setRowFactory(tv -> {
            TableRow<Produto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    Produto produtoSelecionado = row.getItem();
                    adicionarProdutoAVenda(produtoSelecionado);
                }
            });
            return row;
        });

    }
    private void adicionarProdutoAVenda(Produto produto) {
        // Verifica se o produto já está na venda
        Optional<ItemVenda> itemExistente = tableViewItensVenda.getItems().stream()
                .filter(item -> item.getIdProduto() == (produto.getId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            // Se já existe, aumenta a quantidade
            ItemVenda item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + 1);
            item.getTotalItem();
            tableViewItensVenda.refresh(); // Atualiza a tabela
        } else {
            // Se não existe, cria novo item
            ItemVenda novoItem = new ItemVenda(produto, 1);

            tableViewItensVenda.getItems().add(novoItem);
        }

        atualizarTotais();
    }

    private void configurarTabelas() {
        // Tabela de produtos
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colDescricao.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colValorUnitario.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        // Tabela de itens da venda
        colItemDescricao.setCellValueFactory(cellData -> cellData.getValue().getProduto().nomeProperty());
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
        // Configura um Timeline que atualiza a cada 1 segundo
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), // Atualiza a cada segundo
                        event -> {
                            String dataHoraAtual = LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                            lblDataVenda.setText(dataHoraAtual);
                        }
                ));

        timeline.setCycleCount(Timeline.INDEFINITE); // Executa indefinidamente
        timeline.play(); // Inicia a atualização
        lblCliente.setText("CLIENTE PADRÃO");
        txtQuantidade.setText("1");
        lblTotalBruto.setText("R$ 0,00");
        lblTotalLiquido.setText("R$ 0,00");
        lblTotalPago.setText("R$ 0,00");
        lblTroco.setText("R$ 0,00");

        discountAmount = BigDecimal.ZERO;
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

    private void cancelarVenda(){
        itensVenda.clear();
        txtValorPgto1.clear();
        discountAmount = BigDecimal.ZERO;
        atualizarTotais();
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
                ItemVenda novoItem = new ItemVenda(produto, quantidade);
                itensVenda.add(novoItem);
            }

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
        BigDecimal totalLiquido = totalBruto.subtract(discountAmount);
        if (totalLiquido.compareTo(BigDecimal.ZERO) < 0) {
            totalLiquido = BigDecimal.ZERO; // total no negative
        }
        lblTotalLiquido.setText(currencyFormat.format(totalLiquido));
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

                // Trata vírgula como separador decimal
                String input = desconto.replace(",", ".").replace("%", "");

                // Verifica se o valor é negativo
                if (input.startsWith("-")) {
                    mostrarAlerta("Erro", "O desconto não pode ser negativo.");
                    return;
                }

                // Calcula o desconto
                if (desconto.contains("%")) {
                    double percentual = Double.parseDouble(input) / 100;
                    valorDesconto = total.multiply(BigDecimal.valueOf(percentual));
                } else {
                    valorDesconto = new BigDecimal(input);
                }

                // Garante que o desconto não seja maior que o total
                if (valorDesconto.compareTo(total) > 0) {
                    mostrarAlerta("Aviso", "Desconto maior que o total da venda. Ajustado para o valor total.");
                    valorDesconto = total;
                }

                // Atualiza o desconto armazenado
                discountAmount = valorDesconto;

                // Atualiza a interface
                BigDecimal totalLiquido = total.subtract(valorDesconto);
                if (totalLiquido.compareTo(BigDecimal.ZERO) < 0) {
                    totalLiquido = BigDecimal.ZERO; // evita negativo
                }
                lblTotalLiquido.setText(currencyFormat.format(totalLiquido));

                // Recalcula o troco com o novo total
                calcularTroco();

            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Valor inválido. Use números (ex: 10 ou 10.5%)");
            }
        });
    }

    @FXML
    private void novaVenda() {
        itensVenda.clear();
        txtValorPgto1.clear();
        discountAmount = BigDecimal.ZERO;  // reset discount
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

        // Validação dos itens
        for (ItemVenda item : itensVenda) {
            if (item.getQuantidade() <= 0 || item.getPrecoUnitario() <= 0) {
                mostrarAlerta("Erro", "Quantidade e preço devem ser maiores que zero para todos os itens.");
                return;
            }
        }

        calcularTroco();

        if (lblTroco.getText().equals("Insuficiente")) {
            mostrarAlerta("Aviso", "Valor pago é menor que o total");
            return;
        }

        boolean sucesso = vendaService.registrarVenda(itensVenda, "DINHEIRO");

        if (sucesso) {
            // Gera e exibe o comprovante
            String comprovante = gerarComprovanteVenda();
            exibirComprovante(comprovante);

            mostrarAlerta("Sucesso", "Venda registrada");
            novaVenda();
        } else {
            mostrarAlerta("Erro", "Falha ao registrar venda");
        }
    }
    private void exibirComprovante(String comprovante) {
        // 1. Cria uma janela de visualização
        TextArea textArea = new TextArea(comprovante);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: monospace;"); // Fonte de largura fixa para alinhamento correto

        Button btnImprimir = new Button("Imprimir");
        btnImprimir.setOnAction(e -> imprimirComprovante(comprovante));

        VBox root = new VBox(10, textArea, btnImprimir);
        root.setPadding(new Insets(15));

        Stage stage = new Stage();
        stage.setTitle("Comprovante de Venda");
        stage.setScene(new Scene(root, 400, 500));
        stage.show();
    }

    // 2. Método separado para impressão física
    private void imprimirComprovante(String comprovante) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            // Cria um nó de impressão formatado
            Text printerText = new Text(comprovante);
            printerText.setFont(Font.font("Monospaced", 10));
            printerText.setTextAlignment(TextAlignment.LEFT);

            StackPane printRoot = new StackPane(printerText);
            printRoot.setPadding(new Insets(20));

            boolean success = job.printPage(printRoot);
            if (success) {
                job.endJob();
            } else {
                mostrarAlerta("Erro", "Falha ao imprimir comprovante.");
            }
        }
    }

    private String gerarComprovanteVenda() {
        StringBuilder comprovante = new StringBuilder();

        // Cabeçalho
        comprovante.append("----------------------------------------\n");
        comprovante.append("          COMPROVANTE DE VENDA          \n");
        comprovante.append("----------------------------------------\n");
        comprovante.append("Número: ").append(lblNumeroVenda.getText()).append("\n");
        comprovante.append("Data: ").append(lblDataVenda.getText()).append("\n");
        comprovante.append("Vendedor: ").append(lblVendedor.getText()).append("\n");
        comprovante.append("Cliente: ").append(lblCliente.getText()).append("\n");
        comprovante.append("----------------------------------------\n");
        comprovante.append("ITENS:\n");

        // Itens da venda
        for (ItemVenda item : itensVenda) {
            comprovante.append(String.format(
                    "%-20s %3d x %-8s %10s\n",
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    currencyFormat.format(item.getPrecoUnitario()),
                    currencyFormat.format(item.getPrecoUnitario() * item.getQuantidade())
            ));
        }

        // Totais
        comprovante.append("----------------------------------------\n");
        comprovante.append(String.format("%-30s %10s\n", "TOTAL BRUTO:", lblTotalBruto.getText()));
        comprovante.append(String.format("%-30s %10s\n", "TOTAL LÍQUIDO:", lblTotalLiquido.getText()));
        comprovante.append(String.format("%-30s %10s\n", "VALOR PAGO:", lblTotalPago.getText()));
        comprovante.append(String.format("%-30s %10s\n", "TROCO:", lblTroco.getText()));
        comprovante.append("----------------------------------------\n");
        comprovante.append("Obrigado pela preferência!\n");

        return comprovante.toString();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

