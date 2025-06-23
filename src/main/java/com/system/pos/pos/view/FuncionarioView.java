package com.system.pos.pos.view;

import com.system.pos.pos.controller.FuncionariosController;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Funcionario;
import com.system.pos.pos.service.EnderecoService;
import com.system.pos.pos.report.ReportPrinter;
import com.system.pos.pos.utils.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.system.pos.pos.utils.AlertUtil.mostrarAlerta;

public class FuncionarioView {
    @FXML private TextField nome;
    @FXML private TextField email;
    @FXML private TextField telefone;
    @FXML private TextField cpf;
    @FXML private TextField cargo;
    @FXML private TextField salario;
    @FXML private DatePicker dataAdmissao;
    @FXML private DatePicker dataDemissao;
    @FXML private ComboBox<String> turno;
    @FXML private ComboBox<String> status;
    @FXML private TextField cep;
    @FXML private TableView<Funcionario> table;

    private ObservableList<Funcionario> funcionarios;
    private final FuncionariosController funcionarioController;
    private final EnderecoService enderecoService;

    public FuncionarioView() {
        this.funcionarioController = new FuncionariosController();
        this.enderecoService = new EnderecoService();
    }

    @FXML
    public void initialize() throws SQLException {
        this.funcionarios = FXCollections.observableArrayList();
        inicializarTabela();
        carregarFuncionarios();
        configurarBuscaCEP();
        configurarComboBoxes();
    }

    private void configurarComboBoxes() {
        turno.getItems().addAll("Manhã", "Tarde", "Noite", "Integral");
        status.getItems().addAll("Ativo", "Inativo", "Férias", "Afastado");
    }

    private void configurarBuscaCEP() {
        cep.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !cep.getText().isEmpty()) {
                buscarEnderecoPorCEP();
            }
        });
    }

    private void buscarEnderecoPorCEP() {
        try {
            Endereco endereco = enderecoService.buscarEnderecoPorCep(cep.getText());
        } catch (Exception e) {
            mostrarAlerta("Erro na busca", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void carregarFuncionarios() throws SQLException {
        funcionarios.setAll(funcionarioController.listarTodos());
    }

    @FXML
    private void cadastroFuncionarioBTN() {
        try {
            if (validarCampos()) {
                Endereco endereco = criarEnderecoFromInputs();
                Funcionario funcionario = new Funcionario(
                        null, // TipoParticipante já definido no construtor
                        cpf.getText(),
                        nome.getText(),
                        telefone.getText(),
                        email.getText(),
                        endereco,
                        cargo.getText(),
                        new BigDecimal(salario.getText()),
                        dataAdmissao.getValue(),
                        dataDemissao.getValue(),
                        turno.getValue(),
                        status.getValue()
                );

                funcionarioController.cadastrarFuncionario(funcionario);
                carregarFuncionarios();
                limparCampos();
                mostrarAlerta("Sucesso", "Funcionário cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao cadastrar funcionário: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Endereco criarEnderecoFromInputs() {
        try {
            return enderecoService.buscarEnderecoPorCep(cep.getText());
        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha ao buscar endereço: " + e.getMessage(), Alert.AlertType.ERROR);
            return new Endereco();
        }
    }

    @FXML
    private void atualizarFuncionarioBTN() {
        Funcionario selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                if (validarCampos()) {
                    atualizarFuncionarioFromInputs(selecionado);
                    funcionarioController.atualizarFuncionario(selecionado);
                    carregarFuncionarios();
                    limparCampos();
                    mostrarAlerta("Sucesso", "Funcionário atualizado com sucesso!", Alert.AlertType.INFORMATION);
                }
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao atualizar funcionário: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum funcionário selecionado", Alert.AlertType.WARNING);
        }
    }

    private void atualizarFuncionarioFromInputs(Funcionario funcionario) {
        funcionario.setNome(nome.getText());
        funcionario.setTelefone(telefone.getText());
        funcionario.setEmail(email.getText());
        funcionario.setDocumento(cpf.getText());
        funcionario.setCargo(cargo.getText());
        funcionario.setSalario(new BigDecimal(salario.getText()));
        funcionario.setDataAdmissao(dataAdmissao.getValue());
        funcionario.setDataDemissao(dataDemissao.getValue());
        funcionario.setTurno(turno.getValue());
        funcionario.setStatus(status.getValue());

        Endereco endereco = criarEnderecoFromInputs();
        funcionario.setEndereco(endereco);
    }

    @FXML
    private void deleteFuncionarioBTN() {
        Funcionario selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                funcionarioController.excluirFuncionario(selecionado);
                carregarFuncionarios();
                limparCampos();
                mostrarAlerta("Sucesso", "Funcionário removido com sucesso!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao remover funcionário: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Nenhum funcionário selecionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void gerarPDFButton() {
        ReportPrinter.imprimirTabela(table);
    }

    @FXML
    private void limparCampos() {
        nome.clear();
        telefone.clear();
        cpf.clear();
        email.clear();
        cargo.clear();
        salario.clear();
        dataAdmissao.setValue(null);
        dataDemissao.setValue(null);
        turno.getSelectionModel().clearSelection();
        status.getSelectionModel().clearSelection();
        cep.clear();
        table.getSelectionModel().clearSelection();
    }

    private void inicializarTabela() {
        TableColumn<Funcionario, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Funcionario, String> nomeColumn = new TableColumn<>("NOME");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Funcionario, String> cpfColumn = new TableColumn<>("CPF");
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("documento"));

        TableColumn<Funcionario, String> telefoneColumn = new TableColumn<>("TELEFONE");
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Funcionario, String> emailColumn = new TableColumn<>("EMAIL");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Funcionario, String> cargoColumn = new TableColumn<>("CARGO");
        cargoColumn.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        TableColumn<Funcionario, BigDecimal> salarioColumn = new TableColumn<>("SALÁRIO");
        salarioColumn.setCellValueFactory(new PropertyValueFactory<>("salario"));

        TableColumn<Funcionario, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Funcionario, String> enderecoColumn = new TableColumn<>("ENDEREÇO");
        enderecoColumn.setCellValueFactory(cellData -> {
            Endereco endereco = cellData.getValue().getEndereco();
            return new SimpleStringProperty(endereco != null ? endereco.toString() : "");
        });

        table.setItems(funcionarios);
        table.getColumns().setAll(idColumn, nomeColumn, cpfColumn, telefoneColumn,
                emailColumn, cargoColumn, salarioColumn, statusColumn, enderecoColumn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCampos(newSelection);
            }
        });
    }

    private void preencherCampos(Funcionario funcionario) {
        nome.setText(funcionario.getNome());
        cpf.setText(funcionario.getDocumento());
        telefone.setText(funcionario.getTelefone());
        email.setText(funcionario.getEmail());
        cargo.setText(funcionario.getCargo());
        salario.setText(funcionario.getSalario().toString());
        dataAdmissao.setValue(funcionario.getDataAdmissao());
        dataDemissao.setValue(funcionario.getDataDemissao());
        turno.setValue(funcionario.getTurno());
        status.setValue(funcionario.getStatus());

        Endereco endereco = funcionario.getEndereco();
        if (endereco != null) {
            cep.setText(endereco.getCep());
        }
    }

    private boolean validarCampos() {
        if (nome.getText().isBlank() || cpf.getText().isBlank() || cargo.getText().isBlank()
                || salario.getText().isBlank() || dataAdmissao.getValue() == null || cep.getText().isBlank()) {
            mostrarAlerta("Aviso", "Nome, CPF, Cargo, Salário, Data de Admissão e CEP são campos obrigatórios", Alert.AlertType.WARNING);
            return false;
        }

        try {
            new BigDecimal(salario.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Salário deve ser um valor numérico válido", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }
}