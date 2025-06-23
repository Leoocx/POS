package com.system.pos.pos.repository;

import com.system.pos.pos.model.Conta;
import java.sql.SQLException;
import java.util.List;

public interface ContaRepository {
    void insertConta(Conta conta) throws SQLException;
    void updateConta(Conta conta) throws SQLException;
    void deleteConta(int id) throws SQLException;
    Conta searchByID(int id) throws SQLException;
    List<Conta> showAll(boolean isPagar) throws SQLException;
    void registrarPagamento(int id) throws SQLException;
}