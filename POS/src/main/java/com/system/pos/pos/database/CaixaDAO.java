package com.system.pos.pos.database;

import com.system.pos.pos.model.AberturaFechamentoCaixa;

import java.sql.Connection;

public class CaixaDAO {

    private Connection conexao;

    public CaixaDAO(){
        this.conexao=ConnectionDB.conectar();
    }

    public void abrirCaixa(AberturaFechamentoCaixa caixa){
    }

    public void fecharCaixa(AberturaFechamentoCaixa caixa){

    }

    public void exibirTodos(){

    }



}
