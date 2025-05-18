package com.system.pos.pos.model;

import java.time.LocalDateTime;

public class Estoque {

        private Long id;
        private Produto produto;
        private LocalDateTime dataHora;
        private int quantidadeAlterada;
        private String tipo;
        private String observacao;

        public Produto getProduto() {
            return produto;
        }

        public void setProduto(Produto produto) {
            this.produto = produto;
        }

        public String getObservacao() {
            return observacao;
        }

        public void setObservacao(String observacao) {
            this.observacao = observacao;
        }

        public int getQuantidadeAlterada() {
            return quantidadeAlterada;
        }

        public void setQuantidadeAlterada(int quantidadeAlterada) {
            this.quantidadeAlterada = quantidadeAlterada;
        }

        public LocalDateTime getDataHora() {
            return dataHora;
        }

        public void setDataHora(LocalDateTime dataHora) {
            this.dataHora = dataHora;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

}


