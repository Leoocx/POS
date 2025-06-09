package com.system.pos.pos.service;

import com.system.pos.pos.database.ContaDAO;
import com.system.pos.pos.model.Conta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContaService {
    private static final Logger LOGGER = Logger.getLogger(ContaService.class.getName());
    private final ContaDAO contaDAO;

    public ContaService() {
        this.contaDAO = new ContaDAO();
    }

    public void criarConta(Conta conta) {
        try {
            contaDAO.insertConta(conta);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao criar conta", e);
            throw new RuntimeException("Falha ao criar conta", e);
        }
    }

    public void atualizarConta(Conta conta) {
        try {
            contaDAO.updateConta(conta);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar conta", e);
            throw new RuntimeException("Falha ao atualizar conta", e);
        }
    }

    public void removerConta(int id) {
        try {
            contaDAO.deleteConta(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover conta", e);
            throw new RuntimeException("Falha ao remover conta", e);
        }
    }

    public ObservableList<Conta> listarTodas(boolean isPagar) {
        try {
            List<Conta> contas = contaDAO.showAll(isPagar);
            return FXCollections.observableArrayList(contas);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar contas", e);
            throw new RuntimeException("Falha ao listar contas", e);
        }
    }

    public void registrarPagamento(int id) {
        try {
            contaDAO.registrarPagamento(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao registrar pagamento", e);
            throw new RuntimeException("Falha ao registrar pagamento", e);
        }
    }
}