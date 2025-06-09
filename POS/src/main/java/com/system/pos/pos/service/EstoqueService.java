package com.system.pos.pos.service;

import com.system.pos.pos.model.Estoque;
import com.system.pos.pos.model.Produto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EstoqueService {

    private List<Estoque> historico = new ArrayList<>();

    public void registrarEntrada(Produto produto, int quantidade, String obs) {
        produto.setQuantidade(produto.getQuantidade() + quantidade);

        Estoque mov = new Estoque();
        mov.setProduto(produto);
        mov.setDataHora(LocalDateTime.now());
        mov.setQuantidadeAlterada(quantidade);
        mov.setTipo("ENTRADA");
        mov.setObservacao(obs);

        historico.add(mov);
    }

    public void registrarSaida(Produto produto, int quantidade, String obs) {
        if (produto.getQuantidade() < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente!");
        }

        produto.setQuantidade(produto.getQuantidade() - quantidade);

        Estoque mov = new Estoque();
        mov.setProduto(produto);
        mov.setDataHora(LocalDateTime.now());
        mov.setQuantidadeAlterada(-quantidade);
        mov.setTipo("SAÍDA");
        mov.setObservacao(obs);

        historico.add(mov);
    }

    public List<Estoque> getHistorico() {
        return historico;
    }
}
