package com.system.pos.pos.view;

import com.itextpdf.layout.element.Text;
import com.system.pos.pos.controller.FornecedoresController;
import com.system.pos.pos.model.Categoria;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.model.Produto;
import com.system.pos.pos.model.SubCategoria;
import com.system.pos.pos.service.FornecedorService;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class CadastroFornecedorView {

    @FXML ObservableList<Fornecedor> fornecedores;
    @FXML TableView<Fornecedor> tableView;
    @FXML TextField nome;
    @FXML TextField telefone;
    @FXML TextField email;
    @FXML TextField endereco;
    @FXML TextField cnpj;
    @FXML TextField codigo;
    @FXML TextField cep;
    @FXML TextField logradouro;
    @FXML TextField numero;
    @FXML TextField complemento;
    @FXML TextField bairro;
    @FXML TextField cidade;
    @FXML TextField UF;
    @FXML TextField celular;
    @FXML TextField CNPJ;
    @FXML TextField observacao;

    public static FornecedoresController fornecedoresController;

  @FXML
    public void cadastrarFornecedorButton() {
        try {
            String code = codigo.getText();
            String name = nome.getText();
            String tel = telefone.getText();
            String c = celular.getText();
            String mail = email.getText();
            String cnpj = CNPJ.getText();
            String obs = observacao.getText();
    
    
            if (code.isEmpty() || name.isEmpty() || tel.isEmpty() || c.isEmpty() || mail.isEmpty() || cnpj.isEmpty()) {
                System.out.println("Erro: Todos os campos obrigatórios precisam ser preenchidos.");
                return;
            }
    
            String lg = logradouro.getText();
            int num = Integer.parseInt(numero.getText());
            String cmpl = complemento.getText();
            String b = bairro.getText();
            String city = cidade.getText();
            String uf = UF.getText();
    
            if (lg.isEmpty() || b.isEmpty() || city.isEmpty() || uf.isEmpty()) {
                System.out.println("Erro: Preencha todos os campos do endereço.");
                return;
            }
            Endereco enderecoObj = new Endereco(null, null, 0, null, null, null, null);
            Fornecedor fornecedor = new Fornecedor(name, tel, enderecoObj, mail, cnpj);
    
        
            if (fornecedoresController.cadastrarFornecedor(fornecedor)) {
                showAlert("Sucesso", "Produto cadastrado com sucesso!", AlertType.INFORMATION);
            } else {
                showAlert("Erro", "Falha ao cadastrar o produto.", AlertType.ERROR);
            } clearFields();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao cadastrar o produto: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void clearFields(){
        nome.clear();
        telefone.clear();
        cep.clear();
        logradouro.clear();
        numero.clear();
        complemento.clear();
        bairro.clear();
        cidade.clear();
        UF.clear();
        celular.clear();
        email.clear();
        CNPJ.clear();
        observacao.clear();
    }

}
