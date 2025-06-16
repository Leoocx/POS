package com.system.pos.pos.view;

import com.system.pos.pos.exceptions.BusinessException;
import com.system.pos.pos.exceptions.InsufficientStockException;
import com.system.pos.pos.model.*;
import com.system.pos.pos.service.ProdutoService;
import com.system.pos.pos.service.VendaService;
import com.system.pos.pos.utils.AlertUtil;
import com.system.pos.pos.utils.ComprovanteGenerator;
import com.system.pos.pos.utils.CurrencyFormatter;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.system.pos.pos.utils.AlertUtil.mostrarAlerta;

/**
 * Controller principal para o sistema de Ponto de Venda (PDV)
 * Gerencia toda a interação do usuário com a interface de venda
 */
public class PDV {

    // ============== SERVIÇOS ==============
    private final ProdutoService produtoService;
    private final VendaService vendaService;
    private final CurrencyFormatter currencyFormatter;

    // ============== MODELOS ==============
    private Venda vendaAtual;
    private BigDecimal desconto = BigDecimal.ZERO;

    // ============== COMPONENTES DA INTERFACE ==============
    // Labels
    @FXML private Label lblNumeroVenda;
    @FXML private Label lblDataVenda;
    @FXML private Label lblVendedor;
    @FXML private Label lblCliente;
    @FXML private Label lblTotalBruto;
    @FXML private Label lblTotalLiquido;
    @FXML private Label lblTotalPago;
    @FXML private Label lblTroco;
    @FXML private Label lblQtdItens;

    // Campos de texto
    @FXML private TextField txtBusca;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtValorPgto;

    // Tabelas
    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableView<ItemVenda> tabelaItens;

    // ============== CONSTANTES ==============
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Construtor com injeção de dependências
     */
    public PDV(ProdutoService produtoService, VendaService vendaService) {
        this.produtoService = produtoService;
        this.vendaService = vendaService;
        this.currencyFormatter = new CurrencyFormatter();
    }

    /**
     * Inicialização do controller quando o FXML é carregado
     */
    @FXML
    public void initialize() throws SQLException {
        configurarTabelas();
        configurarListeners();
        configurarAtalhosTeclado();
        iniciarNovaVenda();
        atualizarInterface();
    }

    // ============== CONFIGURAÇÕES INICIAIS ==============
    private void configurarTabelas() throws SQLException {
        // Configura colunas da tabela de produtos
        TableColumn<Produto, Integer> colunaId = (TableColumn<Produto, Integer>) tabelaProdutos.getColumns().get(0);
        colunaId.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());

        TableColumn<Produto, String> colunaNome = (TableColumn<Produto, String>) tabelaProdutos.getColumns().get(1);
        colunaNome.setCellValueFactory(cell -> cell.getValue().nomeProperty());

        TableColumn<Produto, BigDecimal> colunaPreco = (TableColumn<Produto, BigDecimal>) tabelaProdutos.getColumns().get(2);
        colunaPreco.setCellValueFactory(cell -> cell.getValue().precoProperty());
        colunaPreco.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal preco, boolean empty) {
                super.updateItem(preco, empty);
                setText(empty ? null : currencyFormatter.format(preco));
            }
        });

        // Configura colunas da tabela de itens
        TableColumn<ItemVenda, String> colunaItemDescricao = (TableColumn<ItemVenda, String>) tabelaItens.getColumns().get(0);
        colunaItemDescricao.setCellValueFactory(cell -> cell.getValue().getProduto().nomeProperty());

        TableColumn<ItemVenda, Integer> colunaItemQuantidade = (TableColumn<ItemVenda, Integer>) tabelaItens.getColumns().get(1);
        colunaItemQuantidade.setCellValueFactory(cell -> cell.getValue().quantidadeProperty().asObject());

        TableColumn<ItemVenda, String> colunaItemTotal = (TableColumn<ItemVenda, String>) tabelaItens.getColumns().get(2);
        colunaItemTotal.setCellValueFactory(cell -> {
            BigDecimal total = cell.getValue().getTotalItem();
            return new javafx.beans.property.SimpleStringProperty(currencyFormatter.format(total));
        });

        // Carrega produtos
        tabelaProdutos.setItems(produtoService.listarTodosProdutos());
    }

    private void configurarListeners() {
        // Listener para busca de produtos
        txtBusca.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                buscarProdutos();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // Listener para cálculo de troco
        txtValorPgto.textProperty().addListener((obs, oldVal, newVal) -> calcularTroco());

        // Validação de quantidade
        txtQuantidade.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtQuantidade.setText(oldVal);
            }
        });
    }

    private void configurarAtalhosTeclado() {
        txtBusca.getScene().setOnKeyPressed(event -> {
            processarAtalhoTeclado(event);
        });
    }

    // ============== MÉTODOS DE NEGÓCIO ==============
    /**
     * Inicia uma nova venda
     */
    private void iniciarNovaVenda() {
        this.vendaAtual = new Venda();
        this.vendaAtual.setPagamento(new Pagamento());
        this.desconto = BigDecimal.ZERO;

        // Configura dados iniciais
        lblNumeroVenda.setText("001"); // Na prática, viria do banco de dados
        lblVendedor.setText("Operador 1");
        lblCliente.setText("Consumidor Final");
        lblDataVenda.setText(LocalDateTime.now().format(DATE_FORMATTER));

        // Limpa campos
        tabelaItens.getItems().clear();
        txtBusca.clear();
        txtQuantidade.setText("1");
        txtValorPgto.clear();

        atualizarInterface();
    }

    /**
     * Adiciona um item à venda atual
     */
    @FXML
    private void adicionarItem() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

        if (produtoSelecionado == null) {
            mostrarAlerta("Selecione um produto", Alert.AlertType.WARNING);
            return;
        }

        try {
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            adicionarItemVenda(produtoSelecionado, quantidade);
        } catch (NumberFormatException e) {
            mostrarAlerta("Quantidade inválida", Alert.AlertType.ERROR);
        } catch (BusinessException e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Lógica principal para adicionar um item à venda
     */
    private void adicionarItemVenda(Produto produto, int quantidade) throws BusinessException {
        // Validações
        if (quantidade <= 0) {
            throw new BusinessException("Quantidade deve ser maior que zero");
        }

        if (quantidade > produto.getQuantidade()) {
            throw new InsufficientStockException(produto.getQuantidade());
        }

        // Verifica se o produto já está na venda
        Optional<ItemVenda> itemExistente = vendaAtual.getItensVenda().stream()
                .filter(item -> item.getIdProduto() == produto.getId())
                .findFirst();

        if (itemExistente.isPresent()) {
            // Atualiza quantidade do item existente
            ItemVenda item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
        } else {
            // Cria novo item
            ItemVenda novoItem = new ItemVenda(produto, quantidade);
            vendaAtual.adicionarItem(novoItem);
        }

        // Atualiza interface
        tabelaItens.setItems(vendaAtual.getItensVenda());
        atualizarInterface();
        txtQuantidade.setText("1");
        txtBusca.requestFocus();
    }



    /**
     * Aplica desconto à venda
     */
    @FXML
    private void aplicarDesconto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Desconto");
        dialog.setHeaderText("Informe o valor ou percentual de desconto");
        dialog.setContentText("Ex: 10 (R$ 10,00) ou 10% (10% do total)");

        Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(descontoStr -> {
            try {
                BigDecimal valorDesconto = calcularValorDesconto(descontoStr);
                this.desconto = valorDesconto;
                vendaAtual.getPagamento().setDesconto(valorDesconto);
                atualizarInterface();
            } catch (NumberFormatException e) {
                mostrarAlerta("Valor de desconto inválido", Alert.AlertType.ERROR);
            }
        });
    }

    /**
     * Calcula o valor do desconto baseado na entrada do usuário
     */
    private BigDecimal calcularValorDesconto(String entrada) throws NumberFormatException {
        entrada = entrada.replace("%", "").replace(",", ".");

        if (entrada.contains("%")) {
            // Desconto percentual
            double percentual = Double.parseDouble(entrada.replace("%", "")) / 100;
            return vendaAtual.calcularTotal().multiply(BigDecimal.valueOf(percentual));
        } else {
            // Desconto em valor absoluto
            return new BigDecimal(entrada);
        }
    }

    /**
     * Calcula o troco baseado no valor pago
     */
    private void calcularTroco() {
        try {
            BigDecimal valorPago = currencyFormatter.parse(txtValorPgto.getText());
            BigDecimal totalLiquido = vendaAtual.getPagamento().calcularTotalLiquido();

            vendaAtual.getPagamento().setValorPago(valorPago);

            if (valorPago.compareTo(totalLiquido) >= 0) {
                BigDecimal troco = valorPago.subtract(totalLiquido);
                lblTroco.setText(currencyFormatter.format(troco));
            } else {
                lblTroco.setText("Insuficiente");
            }

            lblTotalPago.setText(currencyFormatter.format(valorPago));
        } catch (Exception e) {
            lblTroco.setText("Inválido");
        }
    }

    /**
     * Finaliza a venda atual
     */
    @FXML
    private void finalizarVenda() {
        try {
            validarVenda();

            // Confirmação do usuário
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Venda");
            confirmacao.setHeaderText("Deseja finalizar esta venda?");
            confirmacao.setContentText("Total: " + lblTotalLiquido.getText());

            Optional<ButtonType> resultado = confirmacao.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                vendaService.registrarVenda(vendaAtual);
                gerarComprovante();
                iniciarNovaVenda();
            }
        } catch (BusinessException e) {
            mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Valida se a venda pode ser finalizada
     */
    private void validarVenda() throws BusinessException {
        if (vendaAtual.getItensVenda().isEmpty()) {
            throw new BusinessException("Adicione itens à venda antes de finalizar");
        }

        if (!vendaAtual.isPagamentoValido()) {
            throw new BusinessException("Valor pago é insuficiente");
        }
    }

    /**
     * Gera e exibe o comprovante da venda
     */
    private void gerarComprovante() {
        ComprovanteGenerator generator = new ComprovanteGenerator(vendaAtual);
        String comprovante = generator.gerarComprovante();
        ComprovanteView.mostrarComprovante(comprovante);
    }

    /**
     * Cancela a venda atual
     */
    @FXML
    private void cancelarVenda() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Cancelar Venda");
        confirmacao.setHeaderText("Deseja cancelar esta venda?");
        confirmacao.setContentText("Todos os itens serão removidos");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            iniciarNovaVenda();
        }
    }

    // ============== ATUALIZAÇÃO DA INTERFACE ==============
    /**
     * Atualiza todos os elementos da interface
     */
    private void atualizarInterface() {
        BigDecimal totalBruto = vendaAtual.calcularTotal();
        BigDecimal totalLiquido = totalBruto.subtract(desconto);

        // Atualiza labels
        lblTotalBruto.setText(currencyFormatter.format(totalBruto));
        lblTotalLiquido.setText(currencyFormatter.format(totalLiquido));
        lblQtdItens.setText(String.valueOf(
                vendaAtual.getItensVenda().stream()
                        .mapToInt(ItemVenda::getQuantidade)
                        .sum()
        ));

        // Atualiza pagamento e troco
        if (vendaAtual.getPagamento() != null) {
            vendaAtual.getPagamento().setValorTotal(totalBruto);
            calcularTroco();
        }
    }

    // ============== MANIPULAÇÃO DE TECLADO ==============
    /**
     * Processa atalhos de teclado
     */
    private void processarAtalhoTeclado(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String termo = txtBusca.getText();

            Task<ObservableList<Produto>> task = new Task<>() {
                @Override
                protected ObservableList<Produto> call() throws Exception {
                    return produtoService.buscarProdutosPorNome(termo);
                }
            };

            task.setOnSucceeded(e -> {
                tabelaProdutos.setItems(task.getValue());
            });

            task.setOnFailed(e -> {
                mostrarAlerta("Erro", Alert.AlertType.valueOf(task.getException().getMessage()));
            });

            new Thread(task).start();
        }
    }
    /**
     * Busca produtos conforme texto digitado
     */
    private void buscarProdutos() throws SQLException {
        String termo = txtBusca.getText().trim();
        tabelaProdutos.setItems(produtoService.buscarProdutosPorNome(termo));

        if (!tabelaProdutos.getItems().isEmpty()) {
            tabelaProdutos.getSelectionModel().selectFirst();
        }
    }
    /**
     * Processa a tecla ENTER conforme o campo ativo
     */
    private void processarEnter(KeyEvent event) throws SQLException {
        if (event.getSource() == txtBusca) {
            buscarProdutos();
        } else if (event.getSource() == txtQuantidade) {
            adicionarItem();
        } else if (event.getSource() == txtValorPgto) {
            finalizarVenda();
        }
    }
}