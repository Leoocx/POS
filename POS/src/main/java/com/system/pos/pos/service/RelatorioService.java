package com.system.pos.pos.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RelatorioService {

    private static final Path RELATORIOS_PATH = Paths.get("POS/src/main/java/com/system/pos/pos/report/relatorios");

    public List<Path> listarRelatoriosRecentes() {
        try {
            if (!Files.exists(RELATORIOS_PATH)) {
                System.out.println("Pasta não existe, criando: " + RELATORIOS_PATH.toAbsolutePath());
                Files.createDirectories(RELATORIOS_PATH);
            } else {
                System.out.println("Pasta já existe: " + RELATORIOS_PATH.toAbsolutePath());
            }

            try (Stream<Path> stream = Files.list(RELATORIOS_PATH)) {
                return stream
                        .filter(p -> p.toString().endsWith(".pdf"))
                        .sorted(Comparator.comparingLong(p -> {
                            try {
                                return Files.getLastModifiedTime((Path) p).toMillis();
                            } catch (IOException e) {
                                e.printStackTrace();
                                return 0L;
                            }
                        }).reversed())
                        .limit(10)
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }


    public Path getCaminhoRelatorios() {
        return RELATORIOS_PATH;
    }
}
