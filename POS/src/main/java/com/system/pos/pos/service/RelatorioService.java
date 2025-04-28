package com.system.pos.pos.service;

import java.sql.Connection;

public class RelatorioService {

    private final Connection connection;

    public RelatorioService(Connection connection){
        this.connection=connection;
    }


}
