package com.system.pos.pos.view;

import com.system.pos.pos.controller.ClientesController;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.utils.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

import static com.system.pos.pos.utils.AlertUtil.mostrarAlerta;

public class ClienteView {
    @FXML private TextField nome;
    @FXML private TextField telefone;
    @FXML private TextField cpf;
    @FXML private TextField email;
    @FXML private TextField cep;
    @FXML private TableView<Cliente> table;

    private final ClientesController controller;

    public ClienteView() throws SQLException {
        this.controller = new ClientesController();
    }

    @FXML
    public void initialize() {
        controller.configurarTabela(table);
        carregarClientes();
        configurarListeners();
    }

    private void mostrarEndereco(Endereco endereco) {
        mostrarAlerta("CEP Encontrado"+
                 "Endereço: " + endereco.getLogradouro() + ", " + endereco.getLocalidade(),
                Alert.AlertType.INFORMATION);
    }

    private void mostrarAlertaErroCEP() {
        mostrarAlerta("Erro, CEP não encontrado ou inválido", Alert.AlertType.ERROR);
        cep.requestFocus();
    }
    private void configurarListeners() {
        cep.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !cep.getText().isEmpty()) {
                controller.buscarEnderecoPorCEP(cep.getText())(
                        this::mostrarEndereco,
                        this::mostrarAlertaErroCEP
                );
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCampos(newSelection);
            }
        });
    }

    private void carregarClientes() {
        controller.carregarClientes(table);
    }

    @FXML
    private void cadastrarClienteBTN() {
        controller.cadastrarCliente(
                nome.getText(),
                telefone.getText(),
                cpf.getText(),
                email.getText(),
                cep.getText()
        ).ifPresentOrElse(
                cliente -> {
                    carregarClientes();
                    limparCampos();
                    mostrarAlerta("Sucesso, Cliente cadastrado com sucesso!", Alert.AlertType.INFORMATION);
                },
                () -> mostrarAlerta("Erro, Falha ao cadastrar cliente", Alert.AlertType.ERROR)
        );
    }

    @FXML
    private void atualizarClienteBTN() {
        Cliente selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            controller.atualizarCliente(
                    selecionado.getId(),
                    nome.getText(),
                    telefone.getText(),
                    cpf.getText(),
                    email.getText(),
                    cep.getText()
            ).ifPresentOrElse(
                    cliente -> {
                        carregarClientes();
                        limparCampos();
                        mostrarAlerta("Sucesso, Cliente atualizado com sucesso!", Alert.AlertType.INFORMATION);
                    },
                    () -> mostrarAlerta("Erro, Falha ao atualizar cliente", Alert.AlertType.ERROR)
            );
        } else {
            mostrarAlerta("Aviso, Nenhum cliente selecionado", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void removerClienteBTN() {
        Cliente selecionado = table.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            if (controller.removerCliente(selecionado.getId())) {
                carregarClientes();
                limparCampos();
                mostrarAlerta("Sucesso, Cliente removido com sucesso!", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Erro, Falha ao remover cliente", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso, Nenhum cliente selecionado", Alert.AlertType.WARNING);
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
        cep.clear();
        table.getSelectionModel().clearSelection();
    }

    private void preencherCampos(Cliente cliente) {
        nome.setText(cliente.getNome());
        telefone.setText(cliente.getTelefone());
        cpf.setText(cliente.getDocumento());
        email.setText(cliente.getEmail());
        cep.setText(cliente.getEndereco() != null ? cliente.getEndereco().getCep() : "");
    }
}