package com.system.pos.pos.model;
import java.time.LocalDate;

public class Cliente {
    private int codigo;
    private String nome;
    private LocalDate dataCadastro;
    private String telefone;
    private String celular;
    private TipoCliente tipo;
    private String cpfCNPJ;
    private String rg;
    private String emissor;
    private LocalDate dataEmissao;
    private Endereco endereco;
    private String email;
    private String naturalidade;
    private LocalDate dataNascimento;
    private String estadoCivil;
    private boolean bloqueado;

    public Cliente(TipoCliente tipo, String nome, int codigo, LocalDate dataCadastro, String telefone, String celular, String cpfCNPJ, String rg, String emissor, LocalDate dataEmissao, String email, Endereco endereco, String naturalidade, LocalDate dataNascimento, boolean bloqueado, String estadoCivil) {
        this.tipo = tipo;
        this.nome = nome;
        this.codigo = codigo;
        this.dataCadastro = dataCadastro;
        this.telefone = telefone;
        this.celular = celular;
        this.cpfCNPJ = cpfCNPJ;
        this.rg = rg;
        this.emissor = emissor;
        this.dataEmissao = dataEmissao;
        this.email = email;
        this.endereco = endereco;
        this.naturalidade = naturalidade;
        this.dataNascimento = dataNascimento;
        this.bloqueado = bloqueado;
        this.estadoCivil = estadoCivil;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpfCNPJ() {
        return cpfCNPJ;
    }

    public void setCpfCNPJ(String cpfCNPJ) {
        this.cpfCNPJ = cpfCNPJ;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public void setTipo(TipoCliente tipo) {
        this.tipo = tipo;
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getEmissor() {
        return emissor;
    }

    public void setEmissor(String emissor) {
        this.emissor = emissor;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
}
