package com.system.pos.pos.controller;

import com.system.pos.pos.model.Funcionario;
import com.system.pos.pos.service.FuncionarioService;

import java.sql.SQLException;
import java.util.List;

public class FuncionariosController {
    private final FuncionarioService funcionarioService;

    public FuncionariosController() {
        this.funcionarioService = new FuncionarioService();
    }

    public void cadastrarFuncionario(Funcionario funcionario) throws SQLException {
        funcionarioService.adicionarFuncionario(funcionario);
    }

    public void atualizarFuncionario(Funcionario funcionario) throws SQLException {
        funcionarioService.atualizarFuncionario(funcionario);
    }

    public void excluirFuncionario(Funcionario funcionario) throws SQLException {
        funcionarioService.removerFuncionario(funcionario);
    }

    public List<Funcionario> listarTodos() throws SQLException {
        return funcionarioService.listarTodos();
    }
}