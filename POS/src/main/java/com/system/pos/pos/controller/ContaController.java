package com.system.pos.pos.controller;

import com.system.pos.pos.database.ContaDAO;
import com.system.pos.pos.model.Conta;
import com.system.pos.pos.service.ContaService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContaController {
    private final ContaService contaService;

    public ContaController() {
        this.contaService = new ContaService();
    }

    public List<Conta> listarTodasContas() throws SQLException {
        List<Conta> todasContas = new ArrayList<>();
        todasContas.addAll(contaService.listarTodas(true));  // Contas a pagar
        todasContas.addAll(contaService.listarTodas(false)); // Contas a receber
        return todasContas;
    }
    public void criarConta(Conta conta) {
        contaService.criarConta(conta);
    }

    public void atualizarConta(Conta conta) {
        contaService.atualizarConta(conta);
    }

    public void removerConta(int id) {
        contaService.removerConta(id);
    }

    public void registrarPagamento(int id) {
        contaService.registrarPagamento(id);
    }

    public Conta buscarPorId(int id) {
        return contaService.buscarPorId(id);
    }
}