package com.system.pos.pos.model;

import java.sql.Date;
import java.time.LocalDateTime;

public class Relatorio {
    private String nomeArquivo;
    private Date dataGeracao;
    private String caminho;

    public Relatorio(String nomeArquivo, Date dataGeracao, String caminho) {
        this.nomeArquivo = nomeArquivo;
        this.dataGeracao = dataGeracao;
        this.caminho = caminho;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public Date getDataGeracao() {
        return dataGeracao;
    }

    public String getCaminho() {
        return caminho;
    }

    @Override
    public String toString() {
        return nomeArquivo + " - " + dataGeracao;
    }
}
