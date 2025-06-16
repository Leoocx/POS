package com.system.pos.pos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario extends Participante{

    private String cargo;
    private BigDecimal salario;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private String turno;
    private String status;

    public Funcionario(TipoParticipante tipoParticipante, String documento, String nome, String telefone, String email, Endereco endereco,
                       String cargo, BigDecimal salario, LocalDate dataAdmissao, LocalDate dataDemissao,
                       String turno, String status) {
        super(TipoParticipante.PESSOA_FISICA,documento,nome,telefone,email,endereco);
        this.cargo=cargo;
        this.salario=salario;
        this.dataAdmissao=dataAdmissao;
        this.dataDemissao=dataDemissao;
        this.turno=turno;
        this.status=status;
    }


    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDate getDataDemissao() {
        return dataDemissao;
    }

    public void setDataDemissao(LocalDate dataDemissao) {
        this.dataDemissao = dataDemissao;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
