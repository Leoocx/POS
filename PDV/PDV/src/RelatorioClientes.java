import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.colors.ColorConstants;

import java.io.File;
import java.io.IOException;

public class RelatorioClientes {

    public static void gerarReciboPDF(Cliente cliente) {
        String caminhoArquivo = "teste.pdf";

        try {
            
            PdfWriter writer = new PdfWriter(new File(caminhoArquivo));
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Definir uma página de tamanho personalizado (exemplo: 100mm de largura por 120mm de altura)
            pdfDoc.setDefaultPageSize(new com.itextpdf.kernel.geom.PageSize(100, 120));

            Document document = new Document(pdfDoc);
            document.setMargins(7, 7, 7, 7);  
            document.add(new Paragraph("=====RELATORIO=====")
                    .setFontSize(5)  
                    .setBold()
                    .setFontColor(ColorConstants.DARK_GRAY));


            
            document.add(new Paragraph("|CLIENTE: " + cliente)
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            
            document.add(new Paragraph("|NOME:  " + cliente.getNome())
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

        
            document.add(new Paragraph("|CPF: " + cliente.getCpfCNPJ())
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            
            document.add(new Paragraph("|TELEFONE: " + cliente.getTelefone())
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

        
            document.add(new Paragraph("|E-MAIL: " + cliente.getEmail())
                    .setFontSize(4)
                    .setBold()
                    .setFontColor(ColorConstants.RED));

           
            document.add(new Paragraph("|TIPO: " + cliente.getTipo())
                    .setFontSize(6)
                    .setFontColor(ColorConstants.DARK_GRAY));


            document.close();
            System.out.println("|RELATORIO GERADO:  " + caminhoArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
