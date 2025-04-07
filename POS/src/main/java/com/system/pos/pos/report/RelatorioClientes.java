package com.system.pos.pos.report;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.colors.ColorConstants;
import com.system.pos.pos.model.Cliente;

import java.io.File;
import java.io.IOException;

public class RelatorioClientes {

    public static void gerarReciboPDF(Cliente cliente) {
        String caminhoArquivo = "testeRecibo2.pdf";

        try {
            // Tamanho do cupom (Ajuste a largura e altura conforme necessário)
            PdfWriter writer = new PdfWriter(new File(caminhoArquivo));
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Definir uma página de tamanho personalizado (exemplo: 80mm de largura por 120mm de altura)
            pdfDoc.setDefaultPageSize(new com.itextpdf.kernel.geom.PageSize(100, 120));

            Document document = new Document(pdfDoc);

            // Remover margens para um cupom mais compacto
            document.setMargins(7, 7, 7, 7);  // Margens de 5 unidades

            // Título do recibo estilizado
            document.add(new Paragraph("=====RELATORIO=====")
                    .setFontSize(5)  // Reduzir o tamanho da fonte
                    .setBold()
                    .setFontColor(ColorConstants.DARK_GRAY));


            // Informações do cliente com formatação
            document.add(new Paragraph("|CLIENTE: " + cliente)
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            // Produto
            document.add(new Paragraph("|NOME:  " + cliente.getNome())
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            // Quantidade
            document.add(new Paragraph("|CPF: " + cliente.getCpfCNPJ())
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            // Valor Unitário
            document.add(new Paragraph("|TELEFONE: " + cliente.getTelefone())
                    .setFontSize(4)
                    .setFontColor(ColorConstants.BLACK));

            // Total
            document.add(new Paragraph("|E-MAIL: " + cliente.getEmail())
                    .setFontSize(4)
                    .setBold()
                    .setFontColor(ColorConstants.RED));

            // Mensagem de agradecimento
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
