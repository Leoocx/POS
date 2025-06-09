package com.system.pos.pos.repository;

import com.system.pos.pos.model.ItemVenda;

import java.sql.SQLException;
import java.util.List;

public interface VendaRepository {
    int registrarVenda(List<ItemVenda> itens, String formaPagamento) throws SQLException;
    void registrarItensVenda(int idVenda, List<ItemVenda> itens) throws SQLException;

}
