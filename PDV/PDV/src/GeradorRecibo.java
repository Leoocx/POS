import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.colors.ColorConstants;

import java.io.File;
import java.io.IOException;

public class GeradorRecibo {

    public static void gerarReciboPDF(String cliente, String produto, double valor, int quantidade, double total) {
        String caminhoArquivo = "testeRecibo2.pdf";

        try {

            PdfWriter writer = new PdfWriter(new File(caminhoArquivo));
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Definir uma página de tamanho personalizado (exemplo: 80mm de largura por 120mm de altura)
            pdfDoc.setDefaultPageSize(new com.itextpdf.kernel.geom.PageSize(100, 120));

            Document document = new Document(pdfDoc);

            // Remover margens para um cupom mais compacto
            document.setMargins(7, 7, 7, 7);  // Margens de 5 unidades

            // Título
            document.add(new Paragraph("RECIBO DE VENDA")
                    .setFontSize(5)  // Reduzir o tamanho da fonte
                    .setBold()
                    .setFontColor(ColorConstants.DARK_GRAY));


            // Informações do cliente com formatação
            document.add(new Paragraph("Cliente: " + cliente)
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            document.add(new Paragraph("Produto: " + produto)
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            document.add(new Paragraph("Quantidade: " + quantidade)
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            document.add(new Paragraph("Valor Unitário: R$ " + String.format("%.2f", valor))
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            document.add(new Paragraph("TOTAL: R$ " + String.format("%.2f", total))
                    .setFontSize(4)
                    .setBold()
                    .setFontColor(ColorConstants.RED));

            document.add(new Paragraph("Obrigado pela sua compra!")
                    .setFontSize(6)
                    .setFontColor(ColorConstants.DARK_GRAY));


            document.close();
            System.out.println("Recibo gerado: " + caminhoArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
