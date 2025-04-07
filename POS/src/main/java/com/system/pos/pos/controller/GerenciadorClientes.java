package com.system.pos.pos.controller;

import com.system.pos.pos.database.ClienteDAO;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.TipoCliente;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.time.LocalDate;

public class GerenciadorClientes {

    private ClienteDAO clienteDAO;
    private Connection connection;
    private ObservableList<Cliente> clientes;
    private TableView<Cliente> tableView;
    private ComboBox<String> comboBox;
    private TextField codigo;
    private TextField nome;
    private TextField dataCadastro;
    private TextField telefone;
    private TextField celular;
    private TextField tipo;
    private TextField cpfCNPJ;
    private TextField rg;
    private TextField emissor;
    private TextField dataEmissao;
    private TextField cep;
    private TextField logradouro;
    private TextField numero;
    private TextField complemento;
    private TextField bairro;
    private TextField cidade;
    private TextField UF;
    private TextField email;
    private TextField naturalidade;
    private TextField dataNascimento;
    private TextField estadoCivil;
    private TextField bloqueado;

    public void cadastrarCliente(Button addButton) {
        addButton.setOnAction(e -> {
            try {
                int codigoInt = Integer.parseInt(codigo.getText());
                String nomeStr = nome.getText();
                LocalDate dataCad = LocalDate.parse(dataCadastro.getText());
                String telefoneStr = telefone.getText();
                String celularStr = celular.getText();
                String tipoStr = tipo.getText();
                String cpfOuCnpj = cpfCNPJ.getText();
                String rgStr = rg.getText();
                String emissorStr = emissor.getText();
                LocalDate dataEmissaoDoc = LocalDate.parse(dataEmissao.getText());
                String emailStr = email.getText();
                String c = cep.getText();
                String lg = logradouro.getText();
                int num = Integer.parseInt(numero.getText());
                String cmpl = complemento.getText();
                String b = bairro.getText();
                String city= cidade.getText();
                String uf = UF.getText();

                Endereco enderecoObj = new Endereco(c,lg,num,cmpl,b,city,uf);

                String naturalidadeStr = naturalidade.getText();
                LocalDate dataNasc = LocalDate.parse(dataNascimento.getText());
                boolean bloqueadoBool = Boolean.parseBoolean(bloqueado.getText());
                String estadoCivilStr = estadoCivil.getText();

                Cliente cliente = new Cliente(
                        Enum.valueOf(TipoCliente.class, tipoStr),
                        nomeStr,
                        codigoInt,
                        dataCad,
                        telefoneStr,
                        celularStr,
                        cpfOuCnpj,
                        rgStr,
                        emissorStr,
                        dataEmissaoDoc,
                        emailStr,
                        enderecoObj,
                        naturalidadeStr,
                        dataNasc,
                        bloqueadoBool,
                        estadoCivilStr
                );

                clienteDAO.adicionarCliente(cliente);
                clientes.setAll(clienteDAO.listarClientes());
                clearFields();
                System.out.println("Cliente cadastrado com sucesso!");

            } catch (Exception ex) {
                System.out.println("Erro ao cadastrar cliente: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }


    public void editarCliente(){
    }
    public void deletarCliente(Button deleteButton){
        deleteButton.setOnAction(e->{
        Cliente clienteSelecionado = tableView.getSelectionModel().getSelectedItem();
        if (clienteSelecionado!=null){
            clienteDAO.removerCliente(clienteSelecionado);
            clientes.setAll(clienteDAO.listarClientes());
            clearFields();
           }
        });
    }

    public void buscarCliente(){



    }

    public void validarDados(){
    }

    public void exibirClientes(){
        clienteDAO.listarClientes();
    }

    public void clearFields(){
        codigo.clear();
        nome.clear();
        dataCadastro.clear();
        telefone.clear();
        celular.clear();
        tipo.clear();
        cpfCNPJ.clear();
        rg.clear();
        emissor.clear();
        dataEmissao.clear();
        cep.clear();
        logradouro.clear();
        numero.clear();
        complemento.clear();
        bairro.clear();
        cidade.clear();
        UF.clear();
        email.clear();
        naturalidade.clear();
        dataNascimento.clear();
        estadoCivil.clear();
        bloqueado.clear();
    }




}

