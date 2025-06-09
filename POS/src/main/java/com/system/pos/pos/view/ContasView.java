package com.system.pos.pos.view;

import com.system.pos.pos.controller.ContaController;
import com.system.pos.pos.model.Conta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DoubleStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ContasView {
    @FXML private TextField campoFiltro;
    @FXML private ComboBox<String> tipoContaCombo;
    @FXML private TableView<Conta> tabelaContas;
    @FXML private Button btnNovaConta;
    @FXML private Button btnEditar;
    @FXML private Button btnRegistrar;
    @FXML private Button btnRemover;
    @FXML private Label statusBar;
    @FXML private TextField descricaoField;
    @FXML private TextField valorField;
    @FXML private DatePicker vencimentoPicker;
    @FXML private RadioButton radioPagar;
    @FXML private RadioButton radioReceber;

    private final ToggleGroup tipoToggleGroup = new ToggleGroup();
    private final ContaController contaController;
    private final ObservableList<Conta> todasContas;
    private final FilteredList<Conta> contasFiltradas;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ContasView() {
        this.contaController = new ContaController();
        this.todasContas = FXCollections.observableArrayList();
        this.contasFiltradas = new FilteredList<>(todasContas);
    }

    @FXML
    public void initialize() {
        configurarTabela();
        configurarComponentes();
        carregarDados();
        configurarListeners();
        radioPagar.setToggleGroup(tipoToggleGroup);
        radioReceber.setToggleGroup(tipoToggleGroup);
        radioPagar.setSelected(true);
    }

    private void configurarTabela() {
        TableColumn<Conta, String> descricaoCol = new TableColumn<>("Descrição");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Conta, Double> valorCol = new TableColumn<>("Valor");
        valorCol.setCellValueFactory(new PropertyValueFactory<>("valor"));
        valorCol.setCellFactory(tc -> new TableCell<Conta, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", valor));
                }
            }
        });

        TableColumn<Conta, LocalDate> vencimentoCol = new TableColumn<>("Vencimento");
        vencimentoCol.setCellValueFactory(new PropertyValueFactory<>("vencimento"));
        vencimentoCol.setCellFactory(tc -> new TableCell<Conta, LocalDate>() {
            @Override
            protected void updateItem(LocalDate data, boolean empty) {
                super.updateItem(data, empty);
                if (empty || data == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(data));
                    if (data.isBefore(LocalDate.now())) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    }
                }
            }
        });

        TableColumn<Conta, Boolean> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("pago"));
        statusCol.setCellFactory(tc -> new TableCell<Conta, Boolean>() {
            @Override
            protected void updateItem(Boolean pago, boolean empty) {
                super.updateItem(pago, empty);
                if (empty || pago == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(pago ? "Pago" : "Pendente");
                    setStyle(pago ? "-fx-text-fill: green;" : "-fx-text-fill: orange;");
                }
            }
        });

        tabelaContas.getColumns().setAll(descricaoCol, valorCol, vencimentoCol, statusCol);
        tabelaContas.setItems(contasFiltradas);
    }

    private void configurarComponentes() {
        valorField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), 0.0,
                change -> change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null));

        vencimentoPicker.setValue(LocalDate.now().plusDays(15));
    }

    private void carregarDados() {
        try {
            todasContas.setAll(contaController.listarTodasContas());
            tipoContaCombo.getSelectionModel().selectFirst();
            atualizarStatusBar();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao carregar contas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void configurarListeners() {
        // Filtro por texto
        campoFiltro.textProperty().addListener((obs, oldVal, newVal) ->
                contasFiltradas.setPredicate(conta ->
                        newVal == null || newVal.isEmpty() ||
                                conta.getDescricao().toLowerCase().contains(newVal.toLowerCase())
                )
        );

        // Filtro por tipo
        tipoContaCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.equals("Todas")) {
                contasFiltradas.setPredicate(null);
            } else if (newVal.equals("A Pagar")) {
                contasFiltradas.setPredicate(conta -> conta.isPagar());
            } else {
                contasFiltradas.setPredicate(conta -> !conta.isPagar());
            }
            atualizarStatusBar();
        });

        // Seleção na tabela
        tabelaContas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean itemSelecionado = newVal != null;
            btnEditar.setDisable(!itemSelecionado);
            btnRegistrar.setDisable(!itemSelecionado);
            btnRemover.setDisable(!itemSelecionado);

            if (itemSelecionado) {
                preencherFormulario(newVal);
            }
        });
    }

    @FXML
    private void handleNovaConta() {
        limparFormulario();
        radioPagar.setSelected(true);
    }

    @FXML
    private void handleEditar() {
        Conta selecionada = tabelaContas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            preencherFormulario(selecionada);
        }
    }

    @FXML
    private void handleRegistrar() {
        Conta selecionada = tabelaContas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                contaController.registrarPagamento(selecionada.getId());
                selecionada.setPago(true);
                tabelaContas.refresh();
                mostrarAlerta("Sucesso", "Pagamento registrado com sucesso!", Alert.AlertType.INFORMATION);
                atualizarStatusBar();
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao registrar pagamento: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleRemover() {
        Conta selecionada = tabelaContas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                contaController.removerConta(selecionada.getId());
                carregarDados();
                limparFormulario();
                mostrarAlerta("Sucesso", "Conta removida com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao remover conta: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleSalvar() {
        if (validarFormulario()) {
            try {
                Conta conta = criarContaFromFormulario();

                if (conta.getId() == 0) { // Nova conta
                    contaController.criarConta(conta);
                    mostrarAlerta("Sucesso", "Conta criada com sucesso!", Alert.AlertType.INFORMATION);
                } else { // Edição
                    contaController.atualizarConta(conta);
                    mostrarAlerta("Sucesso", "Conta atualizada com sucesso!", Alert.AlertType.INFORMATION);
                }

                carregarDados();
                limparFormulario();
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao salvar conta: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleLimpar() {
        limparFormulario();
    }

    private boolean validarFormulario() {
        if (descricaoField.getText().isBlank()) {
            mostrarAlerta("Aviso", "A descrição é obrigatória", Alert.AlertType.WARNING);
            return false;
        }

        if (valorField.getText().isBlank() || Double.parseDouble(valorField.getText()) <= 0) {
            mostrarAlerta("Aviso", "O valor deve ser positivo", Alert.AlertType.WARNING);
            return false;
        }

        if (vencimentoPicker.getValue() == null) {
            mostrarAlerta("Aviso", "A data de vencimento é obrigatória", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private Conta criarContaFromFormulario() {
        Conta conta = new Conta();
        conta.setDescricao(descricaoField.getText());
        conta.setValor(Double.parseDouble(valorField.getText()));
        conta.setVencimento(vencimentoPicker.getValue());
        conta.setPagar(radioPagar.isSelected());
        return conta;
    }

    private void preencherFormulario(Conta conta) {
        descricaoField.setText(conta.getDescricao());
        valorField.setText(String.valueOf(conta.getValor()));
        vencimentoPicker.setValue(conta.getVencimento());

        if (conta.isPagar()) {
            tipoToggleGroup.selectToggle(radioPagar);
        } else {
            tipoToggleGroup.selectToggle(radioReceber);
        }
    }

    private void limparFormulario() {
        descricaoField.clear();
        valorField.clear();
        vencimentoPicker.setValue(LocalDate.now().plusDays(15));
        radioPagar.setSelected(true);
        tabelaContas.getSelectionModel().clearSelection();
    }

    private void atualizarStatusBar() {
        long total = contasFiltradas.size();
        long pagas = contasFiltradas.stream().filter(Conta::isPago).count();
        double valorTotal = contasFiltradas.stream().filter(c -> !c.isPago()).mapToDouble(Conta::getValor).sum();

        statusBar.setText(String.format("Total: %d | Pagas: %d | Pendentes: %d | Valor Pendente: R$ %.2f",
                total, pagas, total - pagas, valorTotal));
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}