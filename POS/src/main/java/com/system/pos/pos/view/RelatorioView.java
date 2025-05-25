package com.system.pos.pos.view;

import com.system.pos.pos.model.Relatorio;
import com.system.pos.pos.service.RelatorioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.awt.Desktop;
import java.io.File;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RelatorioView {

    @FXML private ListView<Relatorio> listViewRelatorios;
    @FXML private TextField filtroNome;

    private final RelatorioService service = new RelatorioService();
    private List<Relatorio> relatorios;

    @FXML
    public void initialize() {
        carregarRelatorios();
    }

    public void carregarRelatorios() {
        List<Path> paths = service.listarRelatoriosRecentes();

        // Converter Path -> Relatorio
        relatorios = paths.stream()
                .map(p -> new Relatorio(p.getFileName().toString(), Date.valueOf(LocalDate.now()), p.toAbsolutePath().toString()))
                .toList();

        atualizarLista("");
    }


    public void filtrar() {
        String filtro = filtroNome.getText().toLowerCase();
        atualizarLista(filtro);
    }

    private void atualizarLista(String filtro) {
        List<Relatorio> filtrados = relatorios.stream()
                .filter(r -> r.getNomeArquivo().toLowerCase().contains(filtro))
                .toList();
        listViewRelatorios.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    public void abrirSelecionado(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Relatorio selecionado = listViewRelatorios.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                File arquivo = new File(selecionado.getCaminho());
                if (!arquivo.exists()) {
                    new Alert(Alert.AlertType.ERROR, "Arquivo não encontrado!").show();
                    return;
                }

                new Thread(() -> {
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(arquivo);
                        } else {
                            // fallback para Linux com ProcessBuilder
                            ProcessBuilder pb = new ProcessBuilder("xdg-open", arquivo.getAbsolutePath());
                            pb.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Mostrar alerta na thread JavaFX
                        javafx.application.Platform.runLater(() -> {
                            new Alert(Alert.AlertType.ERROR, "Erro ao abrir o arquivo").show();
                        });
                    }
                }).start();
            }
        }
    }


}
