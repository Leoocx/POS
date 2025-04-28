package com.system.pos.pos.controller;

import com.system.pos.pos.database.FornecedorDAO;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
import com.system.pos.pos.service.FornecedorService;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class FornecedoresController {

    private FornecedorDAO fornecedorDAO;

    private Connection connection;
    private ObservableList<Fornecedor> fornecedores;
    private TableView<Fornecedor> tableView;
    private TextField codigo;
    private TextField nome;
    private TextField telefone;
    private TextField cep;
    private TextField logradouro;
    private TextField numero;
    private TextField complemento;
    private TextField bairro;
    private TextField cidade;
    private TextField UF;
    private TextField celular;
    private TextField email;
    private TextField CNPJ;
    private TextField observacao;

    public void cadastrarFornecedor() throws SQLException {

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

        if (!isValidCNPJ(cnpj)) {
            System.out.println("Erro: CNPJ inválido.");
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
        Endereco enderecoObj = new Endereco(c, lg, num, cmpl, b, city, uf);
        Fornecedor fornecedor = new Fornecedor(code, name, tel, enderecoObj, c, mail, cnpj, obs);

        FornecedorService.cadastrarFornecedor(fornecedor);

        // atualizar a lista de fornecedores na View
        fornecedores.setAll(fornecedorDAO.exibirFornecedores());


        clearFields();
        System.out.println("Fornecedor cadastrado!");
    }

    private boolean isValidCNPJ(String cnpj) {
       return true;
    }

    public void atualizarFornecedor(){

    }

    public void excluirFornecedor(){
        }

    public void exibirFornecedores() throws SQLException {
        fornecedorDAO.exibirFornecedores();
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
