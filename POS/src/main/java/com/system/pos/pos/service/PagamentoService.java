package com.system.pos.pos.service;

import com.system.pos.pos.database.ConnectionDB;

import java.sql.Connection;

public class PagamentoService {

    private final Connection connection;

    public PagamentoService(Connection connection){
        this.connection= ConnectionDB.conectar();
    }

    public void realizarPagamento(){}


    public void cancelarPagamento(){}


}
