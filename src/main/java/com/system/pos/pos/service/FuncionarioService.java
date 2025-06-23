package com.system.pos.pos.service;

import com.system.pos.pos.database.FuncionarioDAO;
import com.system.pos.pos.model.Funcionario;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioService {
    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioService() {
        this.funcionarioDAO = new FuncionarioDAO();
    }

    public void adicionarFuncionario(Funcionario funcionario) throws SQLException {
        validarFuncionario(funcionario);
        funcionarioDAO.adicionarFuncionario(funcionario);
    }

    public void atualizarFuncionario(Funcionario funcionario) throws SQLException {
        validarFuncionario(funcionario);
        funcionarioDAO.atualizarFuncionario(funcionario);
    }

    public void removerFuncionario(Funcionario funcionario) throws SQLException {
        funcionarioDAO.removerFuncionario(funcionario);
    }

    public List<Funcionario> listarTodos() throws SQLException {
        return funcionarioDAO.showAll();
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