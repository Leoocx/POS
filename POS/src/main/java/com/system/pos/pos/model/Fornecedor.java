package com.system.pos.pos.model;
public class Fornecedor {
    private String codigo;
    private String nome;
    private Endereco endereco;
    private String telefone;
    private String email;
    private String cnpj;

    public Fornecedor(String nome, String telefone, Endereco endereco, String email, String cnpj) {
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.email = email;
        this.cnpj = cnpj;
    }

    public Fornecedor(String text, String text2, String text3, Endereco endereco2, String text4) {
        //TODO Auto-generated constructor stub
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
