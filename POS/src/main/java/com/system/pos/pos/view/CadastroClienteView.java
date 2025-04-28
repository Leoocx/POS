package com.system.pos.pos.view;

import com.system.pos.pos.controller.ClientesController;
import com.system.pos.pos.model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CadastroClienteView {


    @FXML
    public TextField nome;
    @FXML
    public TextField telefone;
    @FXML
    public TextField cpf;
    @FXML
    public TextField email;
    @FXML
    public TextField endereco;

    public ClientesController clientesController;

    @FXML
    public void cadastrarClienteButton(){
        if (nome.getText().isEmpty() || telefone.getText().isEmpty() || cpf.getText().isEmpty() || email.getText().isEmpty() || endereco.getText().isEmpty() ){
            System.out.println("Campos obrigatórios não foram preenchidos.");
        } else{
            Cliente cliente = new Cliente(
                nome.getText(),
                    telefone.getText(),
                    cpf.getText(),
                    email.getText(),
                    endereco.getText()
            );
            clientesController.cadastrarCliente(cliente);
        }
    }
    @FXML
    public void gerarPDFButton(){


    }

}
