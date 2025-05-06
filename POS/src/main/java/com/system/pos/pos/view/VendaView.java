package com.system.pos.pos.view;

import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Pagamento;
import com.system.pos.pos.model.StatusVenda;
import com.system.pos.pos.model.Venda;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.Optional;

public class VendaView {
    private TextField codigoField = new TextField();
    private TextField quantidadeField = new TextField();
    private TextField precoUnitarioField = new TextField();
    private TextField descontoField = new TextField();
    private ComboBox<Pagamento> formaPagamentoBox = new ComboBox<>();

    public VBox render() {
        VBox layout = new VBox(10);

        layout.getChildren().addAll(
            new Label("Código:"), codigoField,
            new Label("Quantidade:"), quantidadeField,
            new Label("Preço Unitário:"), precoUnitarioField,
            new Label("Desconto:"), descontoField,
            new Label("Forma de Pagamento:"), formaPagamentoBox
        );

        return layout;
    }

    public Optional<Venda> construirVenda(Cliente cliente) {
        try {
            int codigo = Integer.parseInt(codigoField.getText());
            int quantidade = Integer.parseInt(quantidadeField.getText());
            int precoUnitario = Integer.parseInt(precoUnitarioField.getText());
            float desconto = Float.parseFloat(descontoField.getText());
            Pagamento pagamento = formaPagamentoBox.getValue();
            LocalDateTime agora = LocalDateTime.now();

            float total = (precoUnitario * quantidade) - desconto;

            return Optional.of(new Venda(
                codigo, quantidade, total, StatusVenda.ABERTA,
                desconto, pagamento, agora, cliente, precoUnitario
            ));
        } catch (Exception e) {
            System.out.println("Erro ao construir Venda: " + e.getMessage());
            return Optional.empty();
        }
    }
}
