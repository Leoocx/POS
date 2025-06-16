package com.system.pos.pos.controller;

import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.service.ClienteService;
import com.system.pos.pos.service.EnderecoService;
import com.system.pos.pos.report.ReportPrinter;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Optional;

public class ClientesController {
    private final ClienteService clienteService;
    private final EnderecoService enderecoService;

    public ClientesController() throws SQLException {
        this.clienteService = new ClienteService();
        this.enderecoService = new EnderecoService();
    }

    public void configurarTabela(TableView<Cliente> table) {
        table.getColumns().clear();

        TableColumn<Cliente, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cliente, String> nomeColumn = new TableColumn<>("NOME");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> cpfColumn = new TableColumn<>("CPF");
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("documento"));

        TableColumn<Cliente, String> telefoneColumn = new TableColumn<>("TELEFONE");
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Cliente, String> emailColumn = new TableColumn<>("EMAIL");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(idColumn, nomeColumn, cpfColumn, telefoneColumn, emailColumn);
    }

    public ObservableList<Cliente> carregarClientes() {
        return clienteService.listarTodosObservable();
    }

    public Endereco buscarEnderecoPorCEP(String cep) {
        try {
            return enderecoService.buscarEnderecoPorCep(cep);
        } catch (Exception e) {
            return new Endereco();
        }
    }


    public Optional<Cliente> cadastrarCliente(String nome, String telefone, String cpf, String email, String cep) {
        try {
            if (!validarCampos(nome, cpf, cep)) {
                return Optional.empty();
            }

            return buscarEnderecoPorCEP(cep)
                    .flatMap(endereco -> {
                        Cliente cliente = new Cliente(nome, telefone, cpf, email, endereco);
                        return clienteService.cadastrarCliente(cliente);
                    });
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Cliente> atualizarCliente(int id, String nome, String telefone, String cpf, String email, String cep) {
        try {
            if (!validarCampos(nome, cpf, cep)) {
                return Optional.empty();
            }

            return buscarEnderecoPorCEP(cep)
                    .flatMap(endereco -> clienteService.atualizarCliente(id, nome, telefone, cpf, email, endereco));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public boolean removerCliente(int id) {
        return clienteService.removerCliente(id);
    }

    public void gerarPDF(TableView<Cliente> table) {
        ReportPrinter.imprimirTabela(table);
    }

    private boolean validarCampos(String nome, String cpf, String cep) {
        return !nome.isBlank() && !cpf.isBlank() && !cep.isBlank();
    }
}
