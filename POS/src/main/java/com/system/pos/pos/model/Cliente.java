package com.system.pos.pos.model;

import java.time.LocalDate;

public class Cliente extends Participante {
    private LocalDate dataCadastro;

    public Cliente(TipoParticipante tipoParticipante, String documento, String nome,
                   String telefone, String email, Endereco endereco, LocalDate dataCadastro) {
        super(tipoParticipante, documento, nome, telefone, email, endereco);
        this.dataCadastro = dataCadastro != null ? dataCadastro : LocalDate.now();
    }

    public Cliente(String nome, String telefone, String cpf,
                   String email, Endereco endereco) {
        this(TipoParticipante.PESSOA_FISICA, cpf, nome, telefone, email, endereco, LocalDate.now());
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro != null ? dataCadastro : LocalDate.now();
    }
}