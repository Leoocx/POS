package com.system.pos.pos.database;

import com.system.pos.pos.view.AberturaFechamentoCaixa;

import java.sql.Connection;

public class CaixaDAO {

    private Connection conexao;

    public CaixaDAO(Connection conexao){
        this.conexao=conexao;
    }

    public void abrirCaixa(AberturaFechamentoCaixa caixa){
    }

    public void fecharCaixa(AberturaFechamentoCaixa caixa){

    }

    public void exibirTodos(){

    }



}
