package com.system.pos.pos.report;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.UnitValue;
import com.system.pos.pos.model.Relatorio;
import com.system.pos.pos.service.RelatorioService;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;

public class ReportPrinter {

    public static <T> void imprimirTabela(TableView<T> tabela) {
        Window window = tabela.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Salvar como PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        fileChooser.setInitialFileName("relatorio_tabela.pdf");

        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            try (PdfWriter writer = new PdfWriter(new FileOutputStream(file));
                 PdfDocument pdfDoc = new PdfDocument(writer);
                 Document doc = new Document(pdfDoc)) {

                Table pdfTable = new Table(UnitValue.createPercentArray(tabela.getColumns().size()));
                pdfTable.setWidth(UnitValue.createPercentValue(100));

                // Cabeçalhos
                for (TableColumn<?, ?> col : tabela.getColumns()) {
                    pdfTable.addHeaderCell(col.getText());
                }

                // Linhas
                for (T item : tabela.getItems()) {
                    for (TableColumn<T, ?> col : (ObservableList<TableColumn<T, ?>>) tabela.getColumns()) {
                        Object cellData = col.getCellObservableValue(item).getValue();
                        pdfTable.addCell(cellData != null ? cellData.toString() : "");
                    }
                }

                doc.add(pdfTable);

                // Registra o relatório no sistema
                String nome = file.getName();
                String caminho = file.getAbsolutePath();
                Date data = new Date(System.currentTimeMillis());

                Relatorio relatorio = new Relatorio(nome, data, caminho);
                new RelatorioService().registrarRelatorio(relatorio);

                System.out.println("PDF gerado e relatório registrado com sucesso: " + caminho);

            } catch (Exception e) {
                System.err.println("Erro ao gerar PDF: " + e.getMessage());
            }
        }
    }
}
