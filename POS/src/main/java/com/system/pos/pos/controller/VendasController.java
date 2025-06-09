package com.system.pos.pos.controller;

import com.system.pos.pos.model.*;
import com.system.pos.pos.service.VendaService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class VendasController {

    @FXML private TextField codigoProduto;
    @FXML private TextField quantidade;
    @FXML private TextField precoUnitario;
    @FXML private TextField desconto;
    @FXML private ComboBox<Pagamento> formaPagamento;
    @FXML private Label totalVenda;
    @FXML private Button finalizarButton;

    private final VendaService vendaService = new VendaService();

    public void initialize() {
    //    formaPagamento.setItems(FXCollections.observableArrayList(Pagamento.values()));
        totalVenda.setText("0.00");
    }

    @FXML
    public void finalizarVenda() {
        try {
            int codProd = Integer.parseInt(codigoProduto.getText());
            int qtd = Integer.parseInt(quantidade.getText());
            int preco = Integer.parseInt(precoUnitario.getText());
            float desc = Float.parseFloat(desconto.getText());
            Pagamento pagamento = formaPagamento.getValue();

        //    Cliente cliente = buscarCliente();

            //Venda venda = new Venda(
           //     gerarCodigoVenda(),
           //     qtd,
            //    calcularValorTotal(preco, qtd, desc),
            //    StatusVenda.FINALIZADA,
            //    desc,
            //    pagamento,
            //    LocalDateTime.now(),
            //    cliente,
            //    preco
           // );

            //vendaService.registrarVenda(venda);
            mostrarMensagem("Venda finalizada com sucesso!");

            limparCampos();

        } catch (Exception e) {
            mostrarErro("Erro ao finalizar a venda: " + e.getMessage());
        }
    }

    private float calcularValorTotal(int preco, int qtd, float desconto) {
        return (preco * qtd) - desconto;
    }

    //private int gerarCodigoVenda() {
   //     return vendaService.gerarNovoCodigo(); // simula geração sequencial
   // }

    private void limparCampos() {
        codigoProduto.clear();
        quantidade.clear();
        precoUnitario.clear();
        desconto.clear();
        formaPagamento.getSelectionModel().clearSelection();
        totalVenda.setText("0.00");
    }

    private void mostrarErro(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void mostrarMensagem(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}