package com.system.pos.pos.controller;

import com.system.pos.pos.model.Funcionario;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.service.FuncionarioService;
import com.system.pos.pos.service.EnderecoService;
import com.system.pos.pos.report.ReportPrinter;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class FuncionariosController {
    private final FuncionarioService funcionarioService;
    private final EnderecoService enderecoService;

    public FuncionariosController() throws SQLException {
        this.funcionarioService = new FuncionarioService();
        this.enderecoService = new EnderecoService();
    }

    public void configurarTabela(TableView<Funcionario> table) {
        table.getColumns().clear();

        TableColumn<Funcionario, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Funcionario, String> nomeColumn = new TableColumn<>("NOME");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Funcionario, String> cpfColumn = new TableColumn<>("CPF");
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("documento"));

        TableColumn<Funcionario, String> cargoColumn = new TableColumn<>("CARGO");
        cargoColumn.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        TableColumn<Funcionario, BigDecimal> salarioColumn = new TableColumn<>("SALÁRIO");
        salarioColumn.setCellValueFactory(new PropertyValueFactory<>("salario"));

        TableColumn<Funcionario, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idColumn, nomeColumn, cpfColumn, cargoColumn, salarioColumn, statusColumn);
    }

    public void carregarFuncionarios(TableView<Funcionario> table) {
        table.setItems(funcionarioService.listarTodosObservable());
    }

    public Endereco buscarEnderecoPorCEP(String cep) throws Exception {
        return enderecoService.buscarEnderecoPorCep(cep);
    }

    public Funcionario cadastrarFuncionario(
            String nome, String cpf, String telefone, String email,
            String cargo, BigDecimal salario, LocalDate dataAdmissao,
            LocalDate dataDemissao, String turno, String status, String cep) throws Exception {

        if (!validarCampos(nome, cpf, cargo, salario, dataAdmissao, status, cep)) {
            throw new IllegalArgumentException("Campos obrigatórios não preenchidos corretamente");
        }

        Endereco endereco = enderecoService.buscarEnderecoPorCep(cep);
        return funcionarioService.cadastrarFuncionario(
                nome, cpf, telefone, email, endereco,
                cargo, salario, dataAdmissao, dataDemissao, turno, status
        );
    }

    public Funcionario atualizarFuncionario(
            int id, String nome, String cpf, String telefone, String email,
            String cargo, BigDecimal salario, LocalDate dataAdmissao,
            LocalDate dataDemissao, String turno, String status, String cep) throws Exception {

        if (!validarCampos(nome, cpf, cargo, salario, dataAdmissao, status, cep)) {
            throw new IllegalArgumentException("Campos obrigatórios não preenchidos corretamente");
        }

        Endereco endereco = enderecoService.buscarEnderecoPorCep(cep);
        return funcionarioService.atualizarFuncionario(
                id, nome, cpf, telefone, email, endereco,
                cargo, salario, dataAdmissao, dataDemissao, turno, status
        );
    }

    public void removerFuncionario(int id) throws SQLException {
        funcionarioService.removerFuncionario(id);
    }

    public void gerarPDF(TableView<Funcionario> table) {
        ReportPrinter.imprimirTabela(table);
    }

    private boolean validarCampos(String nome, String cpf, String cargo,
                                  BigDecimal salario, LocalDate dataAdmissao, String status, String cep) {
        return !nome.isBlank() && !cpf.isBlank() && !cargo.isBlank() &&
                salario.compareTo(BigDecimal.ZERO) > 0 && dataAdmissao != null &&
                !status.isBlank() && !cep.isBlank();
    }
}