package com.system.pos.pos.report;

import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.control.TableView;
import javafx.scene.transform.Scale;

public interface ReportPrinter {
    public static <T> void imprimirTabela(TableView<T> tabela) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {

            PageLayout pageLayout = job.getJobSettings().getPageLayout();
            double larguraPagina = pageLayout.getPrintableWidth();
            double alturaPagina = pageLayout.getPrintableHeight();

            double larguraTabela = tabela.getBoundsInParent().getWidth();
            double alturaTabela = tabela.getBoundsInParent().getHeight();

            double escalaX = larguraPagina / larguraTabela;
            double escalaY = alturaPagina / alturaTabela;
            double escala = Math.min(escalaX, escalaY);


            Scale scale = new Scale(escala, escala);
            tabela.getTransforms().add(scale);

            // para imprimir
            boolean sucesso = job.printPage(tabela);
            if (sucesso) {
                job.endJob();
            }

            tabela.getTransforms().remove(scale);
        }
    }

}
