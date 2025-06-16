package com.system.pos.pos.view;

import com.system.pos.pos.controller.FuncionariosController;
import com.system.pos.pos.model.Funcionario;
import com.system.pos.pos.utils.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.sql.SQLException;

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

    private final FuncionariosController controller;

    public FuncionarioView() throws SQLException {
        this.controller = new FuncionariosController();
    }

    @FXML
    public void initialize() {
        configurarComboBoxes();
        controller.configurarTabela(table);
        carregarFuncionarios();
        configurarListeners();
    }

    private void configurarComboBoxes() {
        turno.getItems().addAll("Manhã", "Tarde", "Noite", "Integral");
        status.getItems().addAll("Ativo", "Inativo", "Férias", "Afastado");
    }

    private void configurarListeners() {
        cep.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !cep.getText().isEmpty()) {
                controller.buscarEnderecoPorCEP(cep.getText())
                        .ifPresentOrElse(
                                endereco -> {}, // Pode preencher outros campos se necessário
                                () -> mostrarAlerta("Erro, CEP não encontrado", Alert.AlertType.ERROR)
                        );
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCampos(newSelection);
            }
        });
    }

    private void carregarFuncionarios() {
        controller.carregarFuncionarios(table);
    }

    @FXML
    private void cadastrarFuncionarioBTN() {
        try {
            BigDecimal salarioValue = new BigDecimal(salario.getText());

            controller.cadastrarFuncionario(
                    nome.getText(),
                    cpf.getText(),
                    telefone.getText(),
                    email.getText(),
                    cargo.getText(),
                    salarioValue,
                    dataAdmissao.getValue(),
                    dataDemissao.getValue(),
                    turno.getValue(),
                    status.getValue(),
                    cep.getText()
            ).ifPresentOrElse(
                    funcionario -> {
                        carregarFuncionarios();
                        limparCampos();
                        mostrarAlerta("Sucesso, Funcionário cadastrado com sucesso!", Alert.AlertType.INFORMATION);
                    },
                    () -> mostrarAlerta("Erro, Falha ao cadastrar funcionário", Alert.AlertType.ERROR)
            );
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro, Salário deve ser um valor numérico válido", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void atualizarFuncionarioBTN() {
        Funcionario selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                BigDecimal salarioValue = new BigDecimal(salario.getText());

                controller.atualizarFuncionario(
                        selecionado.getId(),
                        nome.getText(),
                        cpf.getText(),
                        telefone.getText(),
                        email.getText(),
                        cargo.getText(),
                        salarioValue,
                        dataAdmissao.getValue(),
                        dataDemissao.getValue(),
                        turno.getValue(),
                        status.getValue(),
                        cep.getText()
                ).ifPresentOrElse(
                        funcionario -> {
                            carregarFuncionarios();
                            limparCampos();
                            mostrarAlerta("Sucesso, Funcionário atualizado com sucesso!", Alert.AlertType.INFORMATION);
                        },
                        () -> mostrarAlerta("Erro, Falha ao atualizar funcionário", Alert.AlertType.ERROR)
                );
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro, Salário deve ser um valor numérico válido", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso, Nenhum funcionário selecionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void removerFuncionarioBTN() {
        Funcionario selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            if (controller.removerFuncionario(selecionado.getId())) {
                carregarFuncionarios();
                limparCampos();
                mostrarAlerta("Sucesso, Funcionário removido com sucesso!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Erro, Falha ao remover funcionário", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso, Nenhum funcionário selecionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void gerarPDFButton() {
        controller.gerarPDF(table);
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

        if (funcionario.getEndereco() != null) {
            cep.setText(funcionario.getEndereco().getCep());
        }
    }
}