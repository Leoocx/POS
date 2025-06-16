package com.system.pos.pos.model;

import java.time.LocalDate;

public class Fornecedor extends Participante {
    private String representante;
    private String telefoneRepresentante;
    private String emailRepresentante;
    private LocalDate dataCadastro;

    public Fornecedor(TipoParticipante tipoParticipante, String documento, String nome,
                      String telefone, String email, Endereco endereco,
                      String representante, String telefoneRepresentante,
                      String emailRepresentante, LocalDate dataCadastro) {
        super(tipoParticipante, documento, nome, telefone, email, endereco);
        this.representante = representante;
        this.telefoneRepresentante = telefoneRepresentante;
        this.emailRepresentante = emailRepresentante;
        this.dataCadastro = dataCadastro != null ? dataCadastro : LocalDate.now();
    }

    // Construtor simplificado
    public Fornecedor(String nome, String telefone, String cnpj,
                      String email, Endereco endereco) {
        this(TipoParticipante.PESSOA_JURIDICA, cnpj, nome, telefone, email,
                endereco, null, null, null, LocalDate.now());
    }

    // Getters e Setters
    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getTelefoneRepresentante() {
        return telefoneRepresentante;
    }

    public void setTelefoneRepresentante(String telefoneRepresentante) {
        this.telefoneRepresentante = telefoneRepresentante;
    }

    public String getEmailRepresentante() {
        return emailRepresentante;
    }

    public void setEmailRepresentante(String emailRepresentante) {
        this.emailRepresentante = emailRepresentante;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro != null ? dataCadastro : LocalDate.now();
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "nome='" + getNome() + '\'' +
                ", cnpj='" + getDocumento() + '\'' +
                ", dataCadastro=" + dataCadastro +
                '}';
    }
}