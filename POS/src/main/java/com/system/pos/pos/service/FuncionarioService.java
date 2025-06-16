package com.system.pos.pos.service;

import com.system.pos.pos.database.FuncionarioDAO;
import com.system.pos.pos.model.Funcionario;
import com.system.pos.pos.model.Endereco;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class FuncionarioService {
    private final FuncionarioDAO funcionarioDAO;
    private final ObservableList<Funcionario> funcionariosObservable;

    public FuncionarioService() throws SQLException {
        this.funcionarioDAO = new FuncionarioDAO();
        this.funcionariosObservable = FXCollections.observableArrayList();
        this.funcionariosObservable.setAll(funcionarioDAO.listarTodos());
    }

    public ObservableList<Funcionario> listarTodosObservable() {
        return funcionariosObservable;
    }

    public Funcionario cadastrarFuncionario(
            String nome, String cpf, String telefone, String email, Endereco endereco,
            String cargo, BigDecimal salario, LocalDate dataAdmissao,
            LocalDate dataDemissao, String turno, String status) {

        try {
            Funcionario funcionario = new Funcionario(
                    null, // TipoParticipante
                    cpf, nome, telefone, email, endereco,
                    cargo, salario, dataAdmissao, dataDemissao, turno, status
            );

            validarFuncionario(funcionario);
            funcionarioDAO.adicionarFuncionario(funcionario);
            funcionariosObservable.setAll(funcionarioDAO.listarTodos());
            return funcionario;
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    public Optional<Funcionario> atualizarFuncionario(
            int id, String nome, String cpf, String telefone, String email, Endereco endereco,
            String cargo, BigDecimal salario, LocalDate dataAdmissao,
            LocalDate dataDemissao, String turno, String status) {

        try {
            Funcionario funcionario = new Funcionario(
                    null, // TipoParticipante
                    cpf, nome, telefone, email, endereco,
                    cargo, salario, dataAdmissao, dataDemissao, turno, status
            );
            funcionario.setId(id);

            validarFuncionario(funcionario);
            funcionarioDAO.atualizarFuncionario(funcionario);
            funcionariosObservable.setAll(funcionarioDAO.listarTodos());
            return Optional.of(funcionario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean removerFuncionario(int id) {
        try {
            funcionarioDAO.removerFuncionario(id);
            funcionariosObservable.setAll(funcionarioDAO.listarTodos());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void validarFuncionario(Funcionario funcionario) {
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do funcionário é obrigatório");
        }
        if (funcionario.getDocumento() == null || funcionario.getDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do funcionário é obrigatório");
        }
        if (funcionario.getCargo() == null || funcionario.getCargo().trim().isEmpty()) {
            throw new IllegalArgumentException("Cargo do funcionário é obrigatório");
        }
        if (funcionario.getSalario() == null || funcionario.getSalario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salário do funcionário deve ser maior que zero");
        }
        if (funcionario.getDataAdmissao() == null) {
            throw new IllegalArgumentException("Data de admissão do funcionário é obrigatória");
        }
        if (funcionario.getStatus() == null || funcionario.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status do funcionário é obrigatório");
        }
    }
}