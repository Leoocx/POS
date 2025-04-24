package com.system.pos.pos.service;

import java.sql.Connection;

public class PagamentoService {

    private final Connection connection;

    public PagamentoService(Connection connection){
        this.connection=connection;
    }

    public void realizarPagamento(){}


    public void cancelarPagamento(){}


}
