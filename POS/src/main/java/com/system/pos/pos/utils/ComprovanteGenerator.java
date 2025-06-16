package com.system.pos.pos.utils;

import com.system.pos.pos.model.Venda;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ComprovanteGenerator {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final Venda venda;

    public ComprovanteGenerator(Venda venda) {
        this.venda = venda;
    }

    public String gerarComprovante() {
        StringBuilder sb = new StringBuilder();

        sb.append("----------------------------------------\n");
        sb.append("          COMPROVANTE DE VENDA          \n");
        sb.append("----------------------------------------\n");

        adicionarCabecalho(sb);
        adicionarItens(sb);
        adicionarTotais(sb);
        adicionarRodape(sb);

        return sb.toString();
    }

    private void adicionarCabecalho(StringBuilder sb) {
        sb.append("Data: ").append(LocalDateTime.now().format(DATE_FORMATTER)).append("\n");
        // ... outras informações do cabeçalho
    }

    private void adicionarItens(StringBuilder sb) {
        sb.append("ITENS:\n");
        venda.getItensVenda().forEach(item ->
                sb.append(String.format("%-20s %3d x %-8s %10s\n",
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        CurrencyFormatter.format(item.getPrecoUnitario()),
                        CurrencyFormatter.format(item.getTotalItem())
                ))
        );
    }

    private void adicionarTotais(StringBuilder sb) {
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-30s %10s\n", "TOTAL:",
                CurrencyFormatter.format(venda.calcularTotal())));
    }

    private void adicionarRodape(StringBuilder sb) {
        sb.append("----------------------------------------\n");
        sb.append("Obrigado pela preferência!\n");
    }
}