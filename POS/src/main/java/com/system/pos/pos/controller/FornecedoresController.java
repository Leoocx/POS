package com.system.pos.pos.controller;

import com.system.pos.pos.database.FornecedorDAO;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.Fornecedor;
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

    public void cadastrarFornecedor () throws SQLException {

        String code = codigo.getText();
        String name = nome.getText();
        String tel = telefone.getText();
        String c = celular.getText();
        String mail=email.getText();
        String cnpj = CNPJ.getText();
        String obs = observacao.getText();

        String lg = logradouro.getText();
        int num = Integer.parseInt(numero.getText());
        String cmpl = complemento.getText();
        String b = bairro.getText();
        String city= cidade.getText();
        String uf = UF.getText();

        Endereco enderecoObj = new Endereco(c,lg,num,cmpl,b,city,uf);;
        Fornecedor fornecedor = new Fornecedor(code, name, tel,enderecoObj,c,mail,cnpj,obs);
        fornecedorDAO.adicionarFornecedor(fornecedor);
        fornecedores.setAll(fornecedorDAO.exibirFornecedores());
        clearFields();
        System.out.println("Fornecedor cadastrado!");
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
