package com.system.pos.pos.service;

import com.system.pos.pos.database.ConnectionDB;

import java.sql.Connection;
import java.sql.SQLException;

public class RelatorioService {

    private final Connection connection;

    public RelatorioService(Connection connection){
        this.connection= ConnectionDB.conectar();
    }


    public void gerarRelatorioClientes() throws SQLException {

    }

    public void gerarRelatorioFinanceiro() {

    }

    public void gerarRelatorioVendas() {

    }

}
