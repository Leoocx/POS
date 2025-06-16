package com.system.pos.pos.service;

import com.system.pos.pos.model.Relatorio;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RelatorioService {

    private static final Path REGISTRO_RELATORIOS = Paths.get("relatorios.csv");

    // Registra um novo relatório no arquivo CSV
    public void registrarRelatorio(Relatorio relatorio) {
        try {
            Files.writeString(REGISTRO_RELATORIOS,
                    String.format("%s;%s;%s%n", relatorio.getNomeArquivo(), relatorio.getDataGeracao(), relatorio.getCaminho()),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carrega todos os relatórios registrados, validando se os arquivos ainda existem
    public List<Relatorio> listarRelatoriosRegistrados() {
        if (!Files.exists(REGISTRO_RELATORIOS)) return List.of();

        try {
            return Files.lines(REGISTRO_RELATORIOS)
                    .map(linha -> {
                        String[] partes = linha.split(";");
                        if (partes.length < 3) return null;

                        String nome = partes[0];
                        Date data = Date.valueOf(partes[1]);
                        String caminho = partes[2];

                        File arquivo = new File(caminho);
                        if (!arquivo.exists() || !arquivo.getName().endsWith(".pdf")) return null;

                        return new Relatorio(nome, data, caminho);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
