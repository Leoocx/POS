package com.system.pos.pos.model;
import java.time.LocalDate;

public class Cliente {
    private int id;
    private String nome;
    private LocalDate dataCadastro;
    private String telefone;
    private String cpf;
    private String endereco;
    private String email;


    public Cliente(String nome, String telefone, String cpf, String email, String endereco ) {
        this.nome=nome;
        this.telefone=telefone;
        this.cpf=cpf;
        this.email=email;
        this.endereco=endereco;
    }

    public Cliente(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
